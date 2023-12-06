package com.apapedia.frontend.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public interface OrderService {
    HashMap<Integer, Integer> getGraph();
}
