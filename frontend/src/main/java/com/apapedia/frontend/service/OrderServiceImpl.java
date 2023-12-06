package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.frontend.DTO.request.LoginRequestDTO;
import com.apapedia.frontend.DTO.request.TokenDTO;
import com.apapedia.frontend.DTO.response.GraphResponseDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrderServiceImpl implements OrderService{

    
    private final WebClient webClient;

    public OrderServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:10141")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public HashMap<Integer, Integer> getGraph()  {
        var response = this.webClient
                .get()
                .uri("/api/order/graph")
                .retrieve()
                .bodyToMono(GraphResponseDTO.class);

        GraphResponseDTO hashmapGraph = response.block();

        return hashmapGraph.getGraph();
    }

    
}
