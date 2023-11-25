package com.apapedia.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;
import com.apapedia.order.repository.OrderDb;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDb orderDb;


    @Override
    public void saveOrder(Order order) {
        orderDb.save(order);
    }
    
}
