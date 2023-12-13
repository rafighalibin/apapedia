package com.apapedia.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.service.RoleService;
import com.apapedia.user.service.UserService;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(UserService userService, RoleService roleService) {
		return args -> {

			roleService.addRole("Seller");
			roleService.addRole("Customer");
		};

	};

}
