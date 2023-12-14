package com.apapedia.order;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.apapedia.order.service.OrderService;

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
