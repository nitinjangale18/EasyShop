package com.service_reg.starter1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Starter1Application {

	public static void main(String[] args) {
		SpringApplication.run(Starter1Application.class, args);
	}

}
