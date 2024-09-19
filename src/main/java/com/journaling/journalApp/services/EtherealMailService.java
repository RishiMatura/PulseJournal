package com.journaling.journalApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EtherealMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${Ethereal-mail-Id}") public String senderID ;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderID); // Set the Ethereal email address
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
