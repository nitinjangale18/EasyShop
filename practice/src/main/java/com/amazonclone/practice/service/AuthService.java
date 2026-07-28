package com.amazonclone.practice.service;

import com.amazonclone.practice.dto.ApiResponse;
import com.amazonclone.practice.dto.RegisterRequest;
import com.amazonclone.practice.dto.LoginRequest;
import com.amazonclone.practice.dto.LoginResponse;
import com.amazonclone.practice.dto.UserProfileResponse;

public interface AuthService {

    ApiResponse register(RegisterRequest request);
    
    LoginResponse login(LoginRequest request);
    
    UserProfileResponse getCurrentUserProfile(String email);

}