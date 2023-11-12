package com.apapedia.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeHttpRequests()
            .requestMatchers("/test").authenticated() // require authentication for /test
            .anyRequest().permitAll() // all other endpoints can be accessed without authentication
        .and()
        .formLogin() // enable form-based login
            .loginPage("/user/login") // specify the login page URL
            .permitAll(); // allow everyone to see the login page

        return http.build();
    }
}
