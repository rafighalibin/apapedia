package com.apapedia.frontend.controller;

import com.apapedia.frontend.service.OrderService;
import com.apapedia.frontend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apapedia.frontend.core.Order;
import com.apapedia.frontend.core.OrderItem;

import java.util.UUID;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderHistoryController {

    @Autowired
    OrderService orderService;
    @GetMapping("/seller/order/history")
    public String sellerHistory(Model model){
        UUID hardSellerId = UUID.fromString("323e4567-e89b-12d3-a456-426614174003"); //belum dynamic berdasarkan login
        List<Order> data = orderService.findBySellerId(hardSellerId);
        model.addAttribute("orders",data);
        System.out.println("Model data: " + model.asMap());
        return "seller-order-history";
    }

}
