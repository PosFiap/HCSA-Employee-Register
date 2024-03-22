package br.com.hackaton.company.employeeregister.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class EmailServiceTest {


    @Mock
    private JavaMailSender emailSender;

    @Spy
    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;


    AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        emailService = new EmailService(emailSender);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void testSendMail() throws MessagingException {

        mimeMessage = new MimeMessage((Session)null);
        emailSender = mock(JavaMailSender.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService = new EmailService(emailSender);


        String recipient = "example@example.com";

        emailService.sendMail(recipient, recipient, recipient);
        assertEquals(recipient, mimeMessage.getRecipients(Message.RecipientType.TO)[0].toString());

    }








}