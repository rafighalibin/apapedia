package com.apapedia.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.OrderService;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @PostMapping(value = "/create")
    public Order tambahOrder(@Valid @RequestBody CreateOrderRequestDTO OrderDTO) {
        var order = orderMapper.createOrderRequestDTOToOrder(OrderDTO);
        orderService.saveOrder(order);
        return order;
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID orderId, @RequestBody Order order) {
        Order existingOrder = orderService.findById(orderId);
        existingOrder.setStatus(order.getStatus());
        orderService.saveOrder(existingOrder);

        return ResponseEntity.ok(existingOrder);
    }
}
