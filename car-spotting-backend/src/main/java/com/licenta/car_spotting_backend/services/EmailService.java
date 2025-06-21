package com.licenta.car_spotting_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Car Spotting!");
        message.setText("Hello " + username + ",\n\n" +
                "Thank you for registering to our service. We hope you enjoy it!\n\n" +
                "Best regards,\n" +
                "The Car Spotting Team");
        mailSender.send(message);
    }
}
