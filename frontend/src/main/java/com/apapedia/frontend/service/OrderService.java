package com.apapedia.frontend.service;

import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.core.Order;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    HashMap<Integer, Integer> getGraph(HttpServletRequest request);

    List<Order> getOrderHistory(HttpServletRequest request, UUID userId);

    String updateOrderStatus(HttpServletRequest request, UUID orderId, int status);


}
