package com.amazone_clone.cart_service.dto;

public class CartItemResponse {

    private Long id;
    private Long productId;
    private String name;
    private Double price;
    private String imageUrl;
    private Integer quantity;
    private Double subtotal;

    public CartItemResponse() {
    }

    public CartItemResponse(
            Long id,
            Long productId,
            String name,
            Double price,
            String imageUrl,
            Integer quantity,
            Double subtotal
    ) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}