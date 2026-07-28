package com.amazonclone.practice.service;

import com.amazonclone.practice.dto.ApiResponse;
import com.amazonclone.practice.dto.RegisterRequest;
import com.amazonclone.practice.entity.User;
import com.amazonclone.practice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amazonclone.practice.dto.LoginRequest;
import com.amazonclone.practice.dto.LoginResponse;
import com.amazonclone.practice.dto.UserProfileResponse;
import com.amazonclone.practice.entity.User;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already registered");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));        
        user.setEnabled(false);
        user.setRole("USER");
   
        userRepository.save(user);

        return new ApiResponse(
                true,
                "User registered successfully"
        );
    }
    
    
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token, "Login successful");
    }
    
    @Override
    public UserProfileResponse getCurrentUserProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
    
    
}