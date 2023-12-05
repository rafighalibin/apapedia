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
	CommandLineRunner run(UserService userService, RoleService roleService){
		return args -> {

			var faker = new Faker();

			roleService.addRole("Seller");
			roleService.addRole("Customer");


			for(int i = 0; i < 2; i++){
				UserModel user = new UserModel();
				user.setRole(roleService.getRoleByRoleName("Seller"));
				user.setBalance((long)100000);
				user.setEmail(faker.internet().emailAddress());
				user.setAddress(faker.address().fullAddress());
				user.setName(faker.name().fullName());
				user.setUsername("user"+i);
				String hashedPass = userService.encrypt("arief123");
				user.setPassword(hashedPass);
				userService.saveUser(user);
			}
			};

		};

}
