package com.amazonclone.practice.controller;

import com.amazonclone.practice.dto.UserProfileResponse;
import com.amazonclone.practice.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonclone.practice.dto.AddressRequest;
import com.amazonclone.practice.entity.Address;
import com.amazonclone.practice.service.AddressService;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;
    private final AddressService addressService;

    public UserController(AuthService authService,AddressService addressService) {
        this.authService = authService;
        this.addressService = addressService;

    }

    @GetMapping("/profile")
    public UserProfileResponse getCurrentUser(Authentication authentication){

        String email = authentication.getName();

        return authService.getCurrentUserProfile(email);
    }
    
    
    @PostMapping("/addresses")
    public Address addAddress(
            Authentication authentication,
            @RequestBody AddressRequest request
    ) {

        String email = authentication.getName();

        return addressService.addAddress(email, request);
    }
    
    @GetMapping("/addresses")
    public List<Address> getMyAddresses(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return addressService.getMyAddresses(email);
    }
    
    @GetMapping("/addresses/{id}")
    public Address getAddressById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return addressService.getAddressById(id, email);
    }
    
}