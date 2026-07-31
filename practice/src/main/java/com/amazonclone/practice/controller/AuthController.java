package com.amazonclone.practice.controller;

import com.amazonclone.practice.dto.ApiResponse;
import com.amazonclone.practice.dto.RegisterRequest;
import com.amazonclone.practice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amazonclone.practice.dto.LoginRequest;
import com.amazonclone.practice.dto.LoginResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
    	System.out.println("Hello Reg api called");

    	
        ApiResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
    	
    	System.out.println("Hello Login api called");

    	
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(
            @RequestParam String token
    ) {
    	System.out.println("Hello verify api called");

        ApiResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }
    
    
    
}