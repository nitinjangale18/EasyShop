package com.amazonclone.practice.service;

public interface EmailService {

    void sendVerificationEmail(String email, String token);
}