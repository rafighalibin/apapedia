package com.apapedia.order.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.OrderItem;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    void saveOrder(Order order);

    Order updateOrderStatus(UUID id, int status, HttpServletRequest servletRequest);

    Order findById(UUID id);

    List<Order> findBySellerId(UUID sellerId);

    List<Order> findByCustomerId(UUID customerId);

    List<Order> findAll();

    void saveOrderItem(OrderItem orderItem);

    HashMap<Integer, Integer> getDailySales(HttpServletRequest request);

}
