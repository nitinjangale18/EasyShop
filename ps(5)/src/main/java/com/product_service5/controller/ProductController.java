package com.product_service5.controller;

import com.product_service5.dto.ProductRequest;
import com.product_service5.dto.ProductResponse;
import com.product_service5.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse response = productService.addProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    
    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }
}