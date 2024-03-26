terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# Config Provider
provider "aws" {
  region = "us-east-1"
  profile = "fiap"
}

# VPC
resource "aws_vpc" "hack_main" {
  cidr_block = "10.0.0.0/23"
  tags = {
    Name = "hack-vpc"
  }
}

# public subnet 1
resource "aws_subnet" "hack_subnet_1" {
  vpc_id                  = aws_vpc.hack_main.id
  cidr_block              = "10.0.0.0/27"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1a"
}

# public subnet 2
resource "aws_subnet" "hack_subnet_2" {
  vpc_id                  = aws_vpc.hack_main.id
  cidr_block              = "10.0.0.32/27"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-1b"
}

# private subnet 1
resource "aws_subnet" "hack_subnet_3" {
  vpc_id                  = aws_vpc.hack_main.id
  cidr_block              = "10.0.1.0/27"
  map_public_ip_on_launch = false
  availability_zone       = "us-east-1b"
}

# Internet Gateway
resource "aws_internet_gateway" "hack_gw" {
  vpc_id = aws_vpc.hack_main.id
}

# public subnet table route
resource "aws_route_table" "hack_route_public" {
  vpc_id = aws_vpc.hack_main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.hack_gw.id
  }
}

# add public subnet 1 in table route
resource "aws_route_table_association" "hack_rota1" {
  subnet_id      = aws_subnet.hack_subnet_1.id
  route_table_id = aws_route_table.hack_route_public.id
}

# add public subnet 2 in table route
resource "aws_route_table_association" "hack_rota2" {
  subnet_id      = aws_subnet.hack_subnet_2.id
  route_table_id = aws_route_table.hack_route_public.id
}

# Nat

# Elastic Ip
resource "aws_eip" "hack_eip" {
  depends_on = [aws_internet_gateway.hack_gw]
  domain     = "vpc"
  tags = {
    Name = "hack_EIP_NAT"
  }
}

# NAT Gateway
resource "aws_nat_gateway" "hack_nat_private_subnet" {
  allocation_id = aws_eip.hack_eip.id
  subnet_id     = aws_subnet.hack_subnet_1.id

  tags = {
    Name = "Hack NAT private subnet"
  }

  depends_on = [aws_internet_gateway.hack_gw]
}

resource "aws_route_table" "hack_route_table_private" {
  vpc_id = aws_vpc.hack_main.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.hack_nat_private_subnet.id
  }
}

resource "aws_route_table_association" "hack_rota3" {
  subnet_id      = aws_subnet.hack_subnet_3.id
  route_table_id = aws_route_table.hack_route_table_private.id
}

# LoadBalancer
resource "aws_alb" "hack_alb" {
  name               = "hack-lb-asg"
  load_balancer_type = "application"
  security_groups    = [aws_security_group.hack_sg_for_alb.id]
  subnets            = [aws_subnet.hack_subnet_1.id, aws_subnet.hack_subnet_2.id]
  depends_on         = [aws_internet_gateway.hack_gw]
}

resource "aws_alb_target_group" "hack_alb_tg" {
  name        = "hack-tf-lb-alb-tg"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.hack_main.id
  target_type = "ip"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_alb_listener" "hack_front_end" {
  load_balancer_arn = aws_alb.hack_alb.arn
  port              = "80"
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.hack_alb_tg.arn
  }
}

# # EC2
# resource "aws_launch_template" "hack_ec2_launch_templ" {
#   name_prefix   = "hack_ec2_launch_templ"
#   image_id      = "ami-080e1f13689e07408"
#   instance_type = "t2.micro"
#
#   network_interfaces {
#     associate_public_ip_address = false
#     subnet_id = aws_subnet.hack_subnet_3.id
#     security_groups = [aws_security_group.hack_sg_for_ec2.id]
#   }
#
#   tag_specifications {
#     resource_type = "instance"
#     tags = {
#       Name = "Hack-instance" # Name for the EC2 instances
#     }
#   }
# }
#
# # AWS Auto Scaling Config
# resource "aws_autoscaling_group" "hack_asg" {
#   # qty of instances
#   desired_capacity = 2
#   max_size         = 9
#   min_size         = 2
#
#   # Connect to the target group
#   target_group_arns = [aws_lb_target_group.hack_alb_tg.arn]
#
#   # EC2 instance in private sbnet
#   vpc_zone_identifier = [
#     aws_subnet.hack_subnet_3.id
#   ]
#
#   launch_template {
#     id      = aws_launch_template.hack_ec2_launch_templ.id
#     version = "$Latest"
#   }
# }

# Security Group

# Sg to elb
resource "aws_security_group" "hack_sg_for_alb" {
  name   = "hack-sg_for_elb"
  vpc_id = aws_vpc.hack_main.id

  ingress {
    description      = "Allow http request from anywhere"
    protocol         = "tcp"
    from_port        = 80
    to_port          = 80
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    description      = "Allow https request from anywhere"
    protocol         = "tcp"
    from_port        = 443
    to_port          = 443
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  ingress {
    protocol         = "icmp"
    from_port        = 8
    to_port          = 0
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

}

# sg to ec2
resource "aws_security_group" "hack_sg_for_ec2" {
  name   = "hack-sg_for_ec2"
  vpc_id = aws_vpc.hack_main.id

  ingress {
    description     = "Allow http request from Load Balancer"
    protocol        = "tcp"
    from_port       = 80 # range of
    to_port         = 80 # port numbers
    security_groups = [aws_security_group.hack_sg_for_alb.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ECR repository to store our Docker images
resource "aws_ecr_repository" "hackaton_app" {
  name = "hack_ecr"
}

# ECS cluster
resource "aws_ecs_cluster" "hack_ecs_cluster" {
  name = "hack-ecs-cluster"
}

# Cloudwatch Log Group
resource "aws_cloudwatch_log_group" "hack_log" {
  name = "openjobs"

  tags = {
    App = "Hack"
  }
}

/* role that the Amazon ECS container agent and the Docker daemon can assume */
resource "aws_iam_role" "ecs_execution_role_task" {
  name               = "ecs_task_execution_role"
  assume_role_policy = file("./modules/policies/ecs_task_definition_role.json")
}

resource "aws_iam_role_policy" "ecs_execution_role_policy" {
  name   = "ecs_execution_role_policy"
  policy = file("./modules/policies/ecs_execution_role_policy.json")
  role   = aws_iam_role.ecs_execution_role_task.id
}

# ECS tasks
data "template_file" "hack_template_app_task" {
  template = file("./modules/tasks/app_task_definition.json")

  vars = {
    image        = aws_ecr_repository.hackaton_app.repository_url
    database_url = "postgresql://postgres:012345678910@database.dns:5432/databasename?encoding=utf8&pool=40"
    log_group    = "hack_log"
  }
}

resource "aws_ecs_task_definition" "hack_app" {
  family                   = "hack"
  container_definitions    = data.template_file.hack_template_app_task.rendered
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_execution_role_task.arn
  task_role_arn            = aws_iam_role.ecs_execution_role_task.arn
}

/* sg for ECS */
resource "aws_security_group" "ecs_service" {
  vpc_id      = aws_vpc.hack_main.id
  name        = "hack-sg_for_ecs"
  description = "Allow egress from container"

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8
    to_port     = 0
    protocol    = "icmp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "ecs-service-sg"
  }
}

data "aws_iam_policy_document" "ecs_service_policy" {
  statement {
    effect    = "Allow"
    resources = ["*"]
    actions = [
      "elasticloadbalancing:Describe*",
      "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
      "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
      "ec2:Describe*",
      "ec2:AuthorizeSecurityGroupIngress"
    ]
  }
}

data "aws_iam_policy_document" "ecs_service_role" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ecs.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs_role" {
  name               = "ecs_role"
  assume_role_policy = data.aws_iam_policy_document.ecs_service_role.json
}

/* ecs service scheduler role */
resource "aws_iam_role_policy" "ecs_service_role_policy" {
  name   = "ecs_service_role_policy"
  policy = data.aws_iam_policy_document.ecs_service_policy.json
  role   = aws_iam_role.ecs_role.id
}

data "aws_ecs_task_definition" "hack_ecs_task" {
  task_definition = aws_ecs_task_definition.hack_app.family
}

resource "aws_ecs_service" "hack_ecs_service" {
  name            = "hack_ecs_service"
  task_definition = "${aws_ecs_task_definition.hack_app.family}:${max("${aws_ecs_task_definition.hack_app.revision}", "${data.aws_ecs_task_definition.hack_ecs_task.revision}")}"
  desired_count   = 2
  launch_type     = "FARGATE"
  cluster         = aws_ecs_cluster.hack_ecs_cluster.id

  network_configuration {
    security_groups = [aws_security_group.ecs_service.id]
    subnets         = [aws_subnet.hack_subnet_3]
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.hack_alb_tg.arn
    container_name   = "app-hackaton"
    container_port   = "80"
  }

  depends_on = [aws_iam_role_policy.ecs_service_role_policy, aws_alb_target_group.hack_alb_tg]
}
