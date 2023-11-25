package com.apapedia.order.service;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;

@Service
public interface OrderService {
    void saveOrder(Order order);
}
