package com.backend.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "spring.mail.host")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOTPEmail(String to, String otpCode, Long otpId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otpCode + "\n\n" +
                "OTP ID: " + otpId + "\n\n" +
                "This code will expire in 5 minutes.\n\n" +
                "If you didn't request this OTP, please ignore this email.");

        mailSender.send(message);
    }
}