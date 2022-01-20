package com.example.sgsparks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String EMAIL;

    private final JavaMailSender javaMailSender;

    public void sendMail(String to, String sub, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(EMAIL);
        mailMessage.setSubject(sub);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
