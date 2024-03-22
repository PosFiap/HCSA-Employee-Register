package br.com.hackaton.company.employeeregister.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String destinatario, String assunto, String corpo) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(corpo);
            emailSender.send(message);
            System.out.println("Email enviado com sucesso para: " + destinatario);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Falha ao enviar o email.");
        }
    }



}
