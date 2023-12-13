package com.apapedia.order.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.dto.request.UpdateBalanceRequestDTO;
import com.apapedia.order.dto.response.ReadUserResponseDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.model.OrderItem;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.repository.OrderItemDb;
import com.apapedia.order.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final WebClient webClient;

    public OrderServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apap-140.cs.ui.ac.id/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Autowired
    OrderDb orderDb;

    @Autowired
    OrderItemDb orderItemDb;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void saveOrder(Order order) {
        orderDb.save(order);
    }

    @Override
    public Order updateOrderStatus(UUID id, int status, HttpServletRequest servletRequest) {
        Order existingOrder = findById(id);
        existingOrder.setStatus(status);
        try {
            ReadUserResponseDTO user = new ReadUserResponseDTO();
            user = getUser(servletRequest);
            Long amount = (long) existingOrder.getTotalPrice();
            Long newBalance = user.getBalance() + amount;
            user.setBalance(newBalance);

            UpdateBalanceRequestDTO data = new UpdateBalanceRequestDTO();
            data.setBalance(user.getBalance());

            var response = this.webClient
                    .put()
                    .uri("/api/user/update-balance")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(servletRequest))
                    .header("Cookie", "jwt=" + getJwtFromCookies(servletRequest))
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(UpdateBalanceRequestDTO.class)
                    .block();

        } catch (Exception e) {
            return null;
        }

        saveOrder(existingOrder);

        return existingOrder;
    }

    @Override
    public Order findById(UUID id) {
        return orderDb.findByOrderId(id);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return orderDb.findByCustomerId(customerId);
    }

    @Override
    public List<Order> findBySellerId(UUID sellerId) {
        return orderDb.findBySellerId(sellerId);
    }

    @Override
    public List<Order> findAll() {
        return orderDb.findAll();
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemDb.save(orderItem);
    }

    @Override
    public HashMap<Integer, Integer> getDailySales(HttpServletRequest request) {

        var token = jwtUtils.parseJwt(request);

        var id = jwtUtils.getIdFromJwtToken(token);

        UUID idSeller = UUID.fromString(id);

        var listOrder = findBySellerId(idSeller);

        HashMap<Integer, Integer> productSold = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();

        Calendar calendar = Calendar.getInstance();

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < maxDay; i++) {
            productSold.put(i + 1, 0);

        }

        for (Order order : listOrder) {
            if (now.getMonthValue() == order.getCreatedAt().getMonthValue()) {
                for (OrderItem orderItem : order.getListOrderItem()) {
                    int day = order.getCreatedAt().getDayOfMonth();
                    // System.out.println(productSold.get(day));
                    productSold.put(day, productSold.get(day) + orderItem.getQuantity());

                }
            }
        }

        return productSold;
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

    public ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException {
        var user = this.webClient
                .get()
                .uri("/api/user/get")
                .header("Cookie", "jwt=" + getJwtFromCookies(request))
                .retrieve()
                .bodyToMono(ReadUserResponseDTO.class);

        ReadUserResponseDTO userResponseDTO = user.block();

        return userResponseDTO;
    }
}
