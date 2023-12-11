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

			for (int y = 0; y < 20; y++) {
				Order order = new Order();
				if (y % 3 == 1)
					order.setCreatedAt(LocalDateTime.now());
				else if (y % 3 == 2)
					order.setCreatedAt(LocalDateTime.now().minusDays(2));
				else
					order.setCreatedAt(LocalDateTime.now().plusDays(2));

				order.setUpdatedAt(LocalDateTime.now());
				order.setStatus(1);
				order.setTotalPrice(999999);
				order.setSellerId(UUID.randomUUID());
				order.setCustomerId(UUID.randomUUID());

				orderService.saveOrder(order);

				for (int i = 0; i < 3; i++) {
					OrderItem orderItem = new OrderItem();
					orderItem.setProductId(UUID.randomUUID());
					orderItem.setOrder(order);
					orderItem.setProductName("vamos");
					orderItem.setProductPrice(696969);
					orderItem.setQuantity(5);
					orderService.saveOrderItem(orderItem);
				}
			}
		};

	};

}
