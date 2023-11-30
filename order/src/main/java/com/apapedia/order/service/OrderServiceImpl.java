package com.apapedia.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;
import com.apapedia.order.repository.OrderDb;

import java.util.UUID;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDb orderDb;


    @Override
    public void saveOrder(Order order) {
        orderDb.save(order);
    }

    @Override
    public Order updateOrderStatus(UUID id, int status) {
        Order existingOrder = findById(id);
        existingOrder.setStatus(status);
        saveOrder(existingOrder);
        return existingOrder;
    }

    @Override
    public Order findById(UUID id) {
        return orderDb.findById(id).orElseThrow();
    }
}
