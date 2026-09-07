package com.amazone_clone.Order_service.service;

import com.amazone_clone.Order_service.client.CartClient;
import com.amazone_clone.Order_service.client.ProductClient;
import com.amazone_clone.Order_service.client.UserClient;
import com.amazone_clone.Order_service.dto.AddressResponse;
import com.amazone_clone.Order_service.dto.CartItemResponse;
import com.amazone_clone.Order_service.dto.CartResponse;
import com.amazone_clone.Order_service.dto.CreateOrderRequest;
import com.amazone_clone.Order_service.dto.ProductResponse;
import com.amazone_clone.Order_service.entiry.Order;
import com.amazone_clone.Order_service.entiry.OrderItem;
import com.amazone_clone.Order_service.entiry.OrderStatus;
import com.amazone_clone.Order_service.exception.OrderNotFoundException;
import com.amazone_clone.Order_service.repository.OrderRepository;

import org.springframework.stereotype.Service;
import com.amazone_clone.Order_service.event.OrderItemEvent;
import java.math.BigDecimal;
import java.util.List;


import com.amazone_clone.Order_service.event.OrderCreatedEvent;
import com.amazone_clone.Order_service.event.OrderKafkaProducer;


@Service
public class OrderService {
	private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final UserClient userClient;
    private final OrderKafkaProducer orderKafkaProducer;
    public OrderService(
            OrderRepository orderRepository,
            CartClient cartClient,
            UserClient userClient,
            ProductClient productClient,
            OrderKafkaProducer orderKafkaProducer
    ) {
        this.orderRepository = orderRepository;
        this.cartClient = cartClient;
        this.userClient = userClient;
        this.productClient = productClient;
        this.orderKafkaProducer = orderKafkaProducer;
    }

    public Order createOrder(
            String token,
            String userEmail,
            CreateOrderRequest request
    ) {

        System.out.println(
                "Selected Address ID: " + request.getAddressId()
        );

        // 1. Get user's cart
        CartResponse cart = cartClient.getCart(token);

        // 2. Check cart
        if (cart == null ||
                cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            throw new RuntimeException("Cart is empty");
        }

        // 3. Get selected address from Auth Service
        AddressResponse address =
                userClient.getAddress(
                        request.getAddressId(),
                        token
                );

        // 4. Create Order
        Order order = new Order();

        order.setUserEmail(userEmail);

        // Save selected delivery address
        order.setAddressId(address.getId());
        order.setDeliveryFullName(address.getFullName());
        order.setDeliveryPhoneNumber(address.getPhoneNumber());
        order.setDeliveryAddressLine(address.getAddressLine());
        order.setDeliveryCity(address.getCity());
        order.setDeliveryState(address.getState());
        order.setDeliveryPincode(address.getPincode());

        // Set total
        order.setTotalAmount(
                BigDecimal.valueOf(cart.getTotal())
        );

        order.setStatus(OrderStatus.PENDING);

        // 5. Convert CartItems -> OrderItems
        for (CartItemResponse cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setProductId(
                    cartItem.getProductId()
            );

            orderItem.setProductName(
                    cartItem.getName()
            );

            orderItem.setPrice(
                    BigDecimal.valueOf(cartItem.getPrice())
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }

        // 6. Save order
        Order savedOrder =
                orderRepository.save(order);

        // 7. Clear cart after successful order creation
        
        List<OrderItemEvent> itemEvents = savedOrder.getItems()
                .stream()
                .map(item -> new OrderItemEvent(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();
        		
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserEmail(),
                savedOrder.getTotalAmount(),
                itemEvents
        );

        orderKafkaProducer.sendOrderCreatedEvent(event);
        
        
        cartClient.clearCart();

        return savedOrder;
    }

    public List<Order> getOrdersByUser(String userEmail) {

        return orderRepository.findByUserEmail(userEmail);
    }

    public Order getOrderById(
            Long orderId,
            String userEmail
    ) {

        return orderRepository
                .findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found"
                        )
                );
    }

    public Order cancelOrder(
            Long orderId,
            String userEmail
    ) {

        Order order = orderRepository
                .findByIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found"
                        )
                );

        if (order.getStatus() == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Order is already cancelled"
            );
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {

            throw new RuntimeException(
                    "Completed order cannot be cancelled"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }
    
    
    
    
    
    
    public Order createBuyNowOrder(
            String token,
            String userEmail,
            Long productId,
            Long addressId
    ) {

        // 1. Get product directly from Product Service
        ProductResponse product =
                productClient.getProduct(productId, token);

        // 2. Check product
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStock() == null || product.getStock() <= 0) {
            throw new RuntimeException("Product is out of stock");
        }

        // 3. Get selected address
        AddressResponse address =
                userClient.getAddress(addressId, token);

        if (address == null) {
            throw new RuntimeException("Address not found");
        }

        // 4. Create Order
        Order order = new Order();

        order.setUserEmail(userEmail);

        // Address snapshot
        order.setAddressId(address.getId());
        order.setDeliveryFullName(address.getFullName());
        order.setDeliveryPhoneNumber(address.getPhoneNumber());
        order.setDeliveryAddressLine(address.getAddressLine());
        order.setDeliveryCity(address.getCity());
        order.setDeliveryState(address.getState());
        order.setDeliveryPincode(address.getPincode());

        // 5. Set total
        order.setTotalAmount(
                BigDecimal.valueOf(product.getPrice())
        );

        order.setStatus(OrderStatus.PENDING);

        // 6. Create one OrderItem
        OrderItem orderItem = new OrderItem();

        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());

        orderItem.setPrice(
                BigDecimal.valueOf(product.getPrice())
        );

        orderItem.setQuantity(1);

        orderItem.setOrder(order);

        order.getItems().add(orderItem);

        // 7. Save order
        Order savedOrder =
                orderRepository.save(order);

        // IMPORTANT:
        // Do NOT clear cart here.
        
        
        
        
        List<OrderItemEvent> itemEvents = savedOrder.getItems()
                .stream()
                .map(item -> new OrderItemEvent(
                        item.getProductId(),
                        item.getQuantity()
                ))
                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserEmail(),
                savedOrder.getTotalAmount(),
                itemEvents
        );

        orderKafkaProducer.sendOrderCreatedEvent(event);

        return savedOrder;
    }
    
    
}