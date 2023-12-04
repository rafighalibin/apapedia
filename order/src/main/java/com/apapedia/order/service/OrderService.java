package com.apapedia.order.service;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    void saveOrder(Order order);

    Order updateOrderStatus(UUID id, int status);

    Order findById(UUID id);

    List<Order> findBySellerId(UUID sellerId);
    List<Order> findAll();
}
