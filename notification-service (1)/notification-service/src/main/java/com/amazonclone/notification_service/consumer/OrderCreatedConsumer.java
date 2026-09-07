package com.amazonclone.notification_service.consumer;


import com.amazonclone.notification_service.event.OrderCreatedEvent;
import com.amazonclone.notification_service.service.NotificationEmailService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedConsumer {

    private final NotificationEmailService
            notificationEmailService;

    public OrderCreatedConsumer(
            NotificationEmailService notificationEmailService
    ) {
        this.notificationEmailService =
                notificationEmailService;
        
        
        System.out.println(">>> OrderCreatedConsumer BEAN CREATED <<<");

    }


    @KafkaListener(
            topics = "order-created",
            groupId = "notification-service"
    )
    public void consumeOrderCreated(
            OrderCreatedEvent event
    ) {

        System.out.println(
                "Order received by Notification Service: "
                + event.getOrderId()
        );

        System.out.println(
                "Customer Email: "
                + event.getUserEmail()
        );

        System.out.println(
                "Total Amount: "
                + event.getTotalAmount()
        );


        notificationEmailService.sendOrderConfirmation(
                event.getUserEmail(),
                event.getOrderId(),
                event.getTotalAmount()
        );
    }
}