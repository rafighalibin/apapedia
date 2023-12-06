package com.apapedia.order.controller;

import java.util.HashMap;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.dto.request.GraphRequestDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @PostMapping(value = "/order/create")
    public Order tambahOrder(@Valid @RequestBody CreateOrderRequestDTO OrderDTO) {
        var order = orderMapper.createOrderRequestDTOToOrder(OrderDTO);
        orderService.saveOrder(order);
        return order;
    }

    @GetMapping("/order/graph")
    public GraphRequestDTO graph(){
        HashMap<Integer, Integer> hashmap = orderService.getDailySales();
        GraphRequestDTO graphDTO = new GraphRequestDTO();
        graphDTO.setGraph(hashmap);
        return graphDTO;
    }
}
