package com.apapedia.frontend.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

@Service
public interface OrderService {
    HashMap<Integer, Integer> getGraph(HttpServletRequest request);
}
