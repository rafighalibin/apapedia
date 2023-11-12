package com.apapedia.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.apapedia.user.model.User;
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
	CommandLineRunner run(UserService userService){
		return args -> {

			var faker = new Faker();

			for(int i = 0; i < 10; i++){
				User user = new User();
				user.setBalance(faker.number().numberBetween(0, Long.MAX_VALUE));
				user.setEmail(faker.internet().emailAddress());
				user.setPassword("ariefthegoat");
				user.setAddress(faker.address().fullAddress());
				user.setName(faker.name().fullName());
				user.setUsername("arief"+i);
				if(i % 2 == 0){
					user.setRole("SELLER");
				}else{
					user.setRole("CUSTOMER");
				}
				userService.addUser(user);
			}
			};

		};

}
