package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.frontend.DTO.response.GraphResponseDTO;
import java.util.HashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderServiceImpl implements OrderService{

    
    private final WebClient webClient;

    public OrderServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("https://apap-141.cs.ui.ac.id")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public HashMap<Integer, Integer> getGraph(HttpServletRequest request)  {
        try{
        var response = this.webClient
                .get()
                .uri("/api/order/graph")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                .retrieve()
                .bodyToMono(GraphResponseDTO.class);
        GraphResponseDTO hashmapGraph = response.block();
        return hashmapGraph.getGraph();
        } catch (Exception e){
            return null;
        }
    }

        public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    
}
