package com.apapedia.frontend.service;

import com.apapedia.frontend.DTO.request.UpdateBalanceRequestDTO;
import com.apapedia.frontend.DTO.request.UpdateOrderRequestDTO;
import com.apapedia.frontend.DTO.response.OrderHistoryResponseDTO;
import com.apapedia.frontend.core.Order;
import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.frontend.DTO.response.GraphResponseDTO;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class OrderServiceImpl implements OrderService{

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    HttpClient httpClient = HttpClient
            .create()
            .wiretap(this.getClass().getCanonicalName(),
                    LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
    
    private final WebClient webClient;

    public OrderServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:10141")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .clientConnector(new ReactorClientHttpConnector(httpClient))
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

    @Override
    public List<Order> getOrderHistory(HttpServletRequest request, UUID userId) {
        try{
            var response = this.webClient
                    .get()
                    .uri("/api/order/get/seller/{userId}", userId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                    .retrieve()
                    .bodyToFlux(Order.class);
            return response.collectList().block();
        } catch (Exception e){
            logger.debug(String.valueOf(e));
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String updateOrderStatus(HttpServletRequest request, UUID orderId, int status) {
        UpdateOrderRequestDTO data = new UpdateOrderRequestDTO();
        data.setStatus(status);

        var response = this.webClient
                .put()
                .uri("/api/order/"+orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                .header("Cookie", "jwt=" + getJwtFromCookies(request))
                .bodyValue(data)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
