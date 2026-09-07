package com.product_service5.consumer;

import com.product_service5.event.OrderCreatedEvent;
import com.product_service5.event.OrderItemEvent;
import com.product_service5.service.ProductService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedConsumer {

    private final ProductService
    productService;

    public OrderCreatedConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "product-service"
    )
    public void consumeOrderCreated(OrderCreatedEvent event) {

        System.out.println(
                "Order received by Product Service: "
                        + event.getOrderId()
        );

        if (event.getItems() == null || event.getItems().isEmpty()) {

            System.out.println(
                    "No order items found for order: "
                            + event.getOrderId()
            );

            return;
        }

        for (OrderItemEvent item : event.getItems()) {

            System.out.println(
                    "Product ID: " + item.getProductId()
                            + ", Quantity: " + item.getQuantity()
            );

            productService.decreaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }
    }
}