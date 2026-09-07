package com.amazonclone.notification_service.service;


import java.math.BigDecimal;

public interface NotificationEmailService {

    void sendOrderConfirmation(
            String email,
            Long orderId,
            BigDecimal totalAmount
    );
}
