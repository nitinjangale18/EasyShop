package com.amazone_clone.Order_service.client;

import com.amazone_clone.Order_service.dto.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/api/users/addresses/{id}")
    AddressResponse getAddress(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );
}