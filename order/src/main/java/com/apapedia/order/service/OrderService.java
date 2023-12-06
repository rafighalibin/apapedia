package com.apapedia.order.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.OrderItem;

@Service
public interface OrderService {
    void saveOrder(Order order);

    void saveOrderItem(OrderItem orderItem);

    HashMap<Integer, Integer> getDailySales();

}
