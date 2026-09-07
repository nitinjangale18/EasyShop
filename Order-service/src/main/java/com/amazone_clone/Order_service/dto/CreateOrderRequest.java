package com.amazone_clone.Order_service.dto;

public class CreateOrderRequest {

    private Long addressId;
    private Long productId;

    public CreateOrderRequest() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}