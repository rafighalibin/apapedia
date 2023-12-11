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
import com.apapedia.order.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;


@Service
public class OrderServiceImpl implements OrderService {

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
    public Order updateOrderStatus(UUID id, int status) {
        Order existingOrder = findById(id);
        existingOrder.setStatus(status);
        saveOrder(existingOrder);
        return existingOrder;
    }

    @Override
    public Order findById(UUID id) {
        return orderDb.findByOrderId(id);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId){
        return orderDb.findByCustomerId(customerId);
    }

    @Override
    public List<Order> findBySellerId(UUID sellerId){
        return orderDb.findBySellerId(sellerId);
    }

    @Override
    public List<Order> findAll(){
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
            productSold.put(i+1, 0);

        }

        for (Order order: listOrder) {
            if (now.getMonthValue() == order.getCreatedAt().getMonthValue()) {
                for (OrderItem orderItem: order.getListOrderItem()) {
                    int day = order.getCreatedAt().getDayOfMonth();
                    // System.out.println(productSold.get(day));
                    productSold.put(day, productSold.get(day) + orderItem.getQuantity());

                }
            }
        }

        return productSold;
    }

}
