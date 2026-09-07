package com.amazone_clone.Order_service.entiry;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    // Delivery address information
    private Long addressId;

    private String deliveryFullName;

    private String deliveryPhoneNumber;

    private String deliveryAddressLine;

    private String deliveryCity;

    private String deliveryState;

    private String deliveryPincode;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    public Order() {
    }

    // ID
    public Long getId() {
        return id;
    }

    // User Email
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // Address ID
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    // Delivery Full Name
    public String getDeliveryFullName() {
        return deliveryFullName;
    }

    public void setDeliveryFullName(String deliveryFullName) {
        this.deliveryFullName = deliveryFullName;
    }

    // Delivery Phone Number
    public String getDeliveryPhoneNumber() {
        return deliveryPhoneNumber;
    }

    public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
        this.deliveryPhoneNumber = deliveryPhoneNumber;
    }

    // Delivery Address Line
    public String getDeliveryAddressLine() {
        return deliveryAddressLine;
    }

    public void setDeliveryAddressLine(String deliveryAddressLine) {
        this.deliveryAddressLine = deliveryAddressLine;
    }

    // Delivery City
    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    // Delivery State
    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    // Delivery Pincode
    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public void setDeliveryPincode(String deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }

    // Total Amount
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Status
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // Created At
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Items
    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}