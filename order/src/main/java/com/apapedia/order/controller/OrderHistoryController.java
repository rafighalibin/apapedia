package com.apapedia.order.controller;

import com.apapedia.order.model.Order;
import com.apapedia.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

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
        return "order-history";
    }
}
