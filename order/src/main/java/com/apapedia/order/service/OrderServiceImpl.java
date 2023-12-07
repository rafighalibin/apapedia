package com.apapedia.order.service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.OrderItem;
import com.apapedia.order.repository.OrderDb;
import com.apapedia.order.repository.OrderItemDb;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDb orderDb;

    @Autowired
    OrderItemDb orderItemDb;


    @Override
    public void saveOrder(Order order) {
        orderDb.save(order);
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemDb.save(orderItem);
    }

    @Override
    public HashMap<Integer, Integer> getDailySales() {
        var listOrder = orderDb.findAll();
        HashMap<Integer, Integer> productSold = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        Calendar calendar = Calendar.getInstance();

        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < maxDay; i++) {
            productSold.put(i+1, 0);

        }

        for (Order order: listOrder) {
            if (now.getMonthValue() == order.getCreatedAt().getMonthValue()) {
                for (OrderItem orderItem: order.getListOrderItem()) {
                    int day = order.getCreatedAt().getDayOfMonth();
                    productSold.put(day, productSold.get(day) + orderItem.getQuantity());

                }
            }
        }

        return productSold;
    }
    
}
