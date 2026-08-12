package com.amazone_clone.cart_service.controller;

import com.amazone_clone.cart_service.dto.AddToCartRequest;
import com.amazone_clone.cart_service.dto.CartResponse;
import com.amazone_clone.cart_service.dto.UpdateCartItemRequest;
import com.amazone_clone.cart_service.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Email") String userEmail,
            @Valid @RequestBody AddToCartRequest request
    ) {

        cartService.addToCart(userEmail, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Product added to cart");
    }
    
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("X-User-Email") String userEmail
    ) {

        return ResponseEntity.ok(
                cartService.getCart(userEmail)
        );
    }
    
    
    @PutMapping("/items/{productId}")
    public ResponseEntity<String> updateQuantity(
            @RequestHeader("X-User-Email") String userEmail,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        cartService.updateQuantity(
                userEmail,
                productId,
                request
        );

        return ResponseEntity.ok(
                "Cart item quantity updated"
        );
    }
    
    
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeItem(
            @RequestHeader("X-User-Email") String userEmail,
            @PathVariable Long productId
    ) {

        cartService.removeItem(userEmail, productId);

        return ResponseEntity.ok(
                "Product removed from cart"
        );
    }
    
    
    @DeleteMapping
    public ResponseEntity<String> clearCart(
            @RequestHeader("X-User-Email") String userEmail
    ) {

        cartService.clearCart(userEmail);

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
    
    
}