package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String to, String orderId, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Buyurtma Tasdiqlash: #" + orderId);
        message.setText("Hurmatli " + userName + ",\n\nSizning buyurtmangiz #" + orderId + " qabul qilindi.\nRahmat!");
        mailSender.send(message);
    }
}
