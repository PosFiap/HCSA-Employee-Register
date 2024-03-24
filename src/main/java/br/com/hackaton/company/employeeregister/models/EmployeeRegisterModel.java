package br.com.hackaton.company.employeeregister.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "TB_EMPLOYEE_REGISTERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRegisterModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String registracionCode;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeRegister;


}
