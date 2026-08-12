package com.amazone_clone.cart_service.repository;

import com.amazone_clone.cart_service.entity.Cart;
import com.amazone_clone.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProductId(
            Cart cart,
            Long productId
    );
}