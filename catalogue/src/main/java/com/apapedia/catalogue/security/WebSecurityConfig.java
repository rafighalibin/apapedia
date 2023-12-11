package com.apapedia.catalogue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.apapedia.catalogue.security.jwt.JwtTokenFilter;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers( "/api/catalogue/viewall").permitAll()
                .requestMatchers( "/api/catalogue/search/search?query={query}").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webfilterChain(HttpSecurity http) throws Exception{ 
        http
            .csrf(Customizer.withDefaults())
            .authorizeHttpRequests (requests -> requests
                .requestMatchers (new AntPathRequestMatcher("/css/**")).permitAll() 
                .requestMatchers (new AntPathRequestMatcher("/js/**")).permitAll()
                .anyRequest().authenticated()
            )
        ;
        return http.build();
    }


}
