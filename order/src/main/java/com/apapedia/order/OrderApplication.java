package com.apapedia.order;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import com.apapedia.order.model.*;
import com.apapedia.order.service.OrderService;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(OrderService orderService) {
		return args -> {

		};

	};

}
