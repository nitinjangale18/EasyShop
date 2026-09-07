package com.amazonclone.notification_service.service;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationEmailServiceImpl
        implements NotificationEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationEmailServiceImpl(
            JavaMailSender mailSender
    ) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOrderConfirmation(
            String email,
            Long orderId,
            BigDecimal totalAmount
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(email);

        message.setSubject(
                "Order #" + orderId + " Confirmed"
        );

        message.setText(
                "Hello,\n\n"
                + "Your order #" + orderId
                + " has been successfully placed.\n\n"
                + "Order Total: ₹" + totalAmount
                + "\n\n"
                + "Thank you for shopping with us!\n\n"
                + "Amazon Clone"
        );

        mailSender.send(message);

        System.out.println(
                "Order confirmation email sent to: "
                + email
        );
    }
}