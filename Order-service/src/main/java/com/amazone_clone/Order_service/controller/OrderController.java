package com.amazone_clone.Order_service.controller;

import com.amazone_clone.Order_service.dto.CreateOrderRequest;
import com.amazone_clone.Order_service.entiry.Order;
import com.amazone_clone.Order_service.service.OrderService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody CreateOrderRequest request
    ) {

        Order order = orderService.createOrder(
                token,
                userEmail,
                request
        );

        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userEmail)
        );
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(id, userEmail)
        );
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long id,
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.ok(
                orderService.cancelOrder(id, userEmail)
        );
    }
    
    
    @PostMapping("/buy-now")
    public ResponseEntity<Order> buyNow(
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-User-Email") String userEmail,
            @RequestBody CreateOrderRequest request
    ) {

        Order order = orderService.createBuyNowOrder(
                token,
                userEmail,
                request.getProductId(),
                request.getAddressId()
        );

        return ResponseEntity.ok(order);
    }
    
}