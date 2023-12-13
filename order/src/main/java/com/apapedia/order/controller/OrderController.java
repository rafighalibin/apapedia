package com.apapedia.order.controller;

import com.apapedia.order.dto.OrderMapper;
import com.apapedia.order.dto.request.CreateOrderRequestDTO;
import com.apapedia.order.dto.request.GraphRequestDTO;
import com.apapedia.order.dto.request.UpdateOrderStatusRequestDTO;
import com.apapedia.order.model.Order;
import com.apapedia.order.service.CartService;
import com.apapedia.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderMapper orderMapper;

    @PostMapping(value = "/create")
    public Order tambahOrder(@Valid @RequestBody CreateOrderRequestDTO OrderDTO) {
        var order = orderMapper.createOrderRequestDTOToOrder(OrderDTO);
        orderService.saveOrder(order);
        return order;
    }

    @GetMapping("/graph")
    public GraphRequestDTO graph(HttpServletRequest request) {
        HashMap<Integer, Integer> hashmap = orderService.getDailySales(request);
        GraphRequestDTO graphDTO = new GraphRequestDTO();
        graphDTO.setGraph(hashmap);
        return graphDTO;
    }

    @GetMapping("/get/customer/{customerId}")
    public ResponseEntity<?> getOrderByCustomerId(@PathVariable UUID customerId) {
        var listOrder = orderService.findByCustomerId(customerId);
        return ResponseEntity.ok(listOrder);
    }

    @GetMapping("/get/seller/{sellerId}")
    public ResponseEntity<?> getOrderBySellerId(@PathVariable UUID sellerId) {
        var listOrder = orderService.findBySellerId(sellerId);
        return ResponseEntity.ok(listOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable UUID id,
            @RequestBody UpdateOrderStatusRequestDTO request, HttpServletRequest servletRequest) {
        try {
            Order order = orderService.updateOrderStatus(id, request.getStatus(), servletRequest);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tidak ada order dengan UUID : " + id);
        }
    }
}
