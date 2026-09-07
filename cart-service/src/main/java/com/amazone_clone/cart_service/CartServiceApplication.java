package com.amazone_clone.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class CartServiceApplication {

	public static void main(String[] args) {
		System.out.println("hello1");
	
		SpringApplication.run(CartServiceApplication.class, args);
		
		
		System.out.println("hello2");
	}

}
