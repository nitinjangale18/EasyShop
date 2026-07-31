package com.amazonclone.practice.controller;

import com.amazonclone.practice.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public ApiResponse adminTest() {
        return new ApiResponse(
                true,
                "Admin endpoint accessed successfully"
        );
    }
}