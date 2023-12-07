package com.apapedia.frontend.service;

import com.apapedia.frontend.core.Order;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    HashMap<Integer, Integer> getGraph();

    List<Order> findBySellerId(UUID sellerId);
}
