package com.amazone_clone.cart_service.dto;

import java.util.List;

public class CartResponse {

    private Long cartId;
    private String userEmail;
    private List<CartItemResponse> items;
    private Double total;

    public CartResponse() {
    }

    public CartResponse(
            Long cartId,
            String userEmail,
            List<CartItemResponse> items,
            Double total
    ) {
        this.cartId = cartId;
        this.userEmail = userEmail;
        this.items = items;
        this.total = total;
    }

    public Long getCartId() {
        return cartId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public Double getTotal() {
        return total;
    }
}