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


import com.amazonclone.practice.entity.EmailVerificationToken;
import com.amazonclone.practice.repository.EmailVerificationTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import com.amazonclone.practice.entity.Role;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;
    
    private final EmailVerificationTokenRepository tokenRepository;
    
    private final EmailService emailService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailVerificationTokenRepository tokenRepository,
            EmailService emailService

    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;

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
        user.setEnabled(true);
        user.setRole(Role.USER);   
        userRepository.save(user);
        
          /*     
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken =
                new EmailVerificationToken(
                        token,
                        LocalDateTime.now().plusHours(24),
                        user
                );
            
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getEmail(), token);
        
        */

        return new ApiResponse(
                true,
                "User registered successfully. Please verify your email."
        );     
        
    }
    
    
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        /*
        if (!user.isEnabled()) {
            throw new RuntimeException(
                    "Please verify your email before logging in"
            );
        }
		 */
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(),user.getRole().name());

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
    
    
    @Override
    public ApiResponse verifyEmail(String token) {

        EmailVerificationToken verificationToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid verification token")
                        );

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            return new ApiResponse(
                    false,
                    "Verification token has expired"
            );
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        tokenRepository.delete(verificationToken);

        return new ApiResponse(
                true,
                "Email verified successfully"
        );
    }
    
    
}