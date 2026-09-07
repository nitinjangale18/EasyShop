package com.amazone_clone.Order_service.client;

import com.amazone_clone.Order_service.config.FeignClientConfig;
import com.amazone_clone.Order_service.dto.CartResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
@FeignClient(
        name = "cart-service",
        configuration = FeignClientConfig.class
)
public interface CartClient {

    @GetMapping("/api/cart")
    CartResponse getCart(
            @RequestHeader("Authorization") String token
    );
    
    
    
    @DeleteMapping("/api/cart")
    void clearCart();
    
}


