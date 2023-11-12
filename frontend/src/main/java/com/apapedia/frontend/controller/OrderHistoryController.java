package com.apapedia.frontend.controller;

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

    @GetMapping("/order-history/")
    public String profilePage(Model model) {
        List<Order> orderList = new ArrayList<>();
        UUID order = UUID.randomUUID();
        UUID customer = UUID.randomUUID();
        UUID seller = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Order dataOrder = new Order(order, new Date(), new Date(), 1, 100000, customer, seller);

        List<OrderItem> orderItem = new ArrayList<>();
        orderItem.add(new OrderItem(productId, order, 1, "Gedung A Fasilkom", 100000));
        orderItem.add(new OrderItem(productId, order, 1, "Gedung B Fasilkom", 100000));
        orderItem.add(new OrderItem(productId, order, 1, "Gedung C Fasilkom", 100000));
        
        dataOrder.setOrderItem(orderItem);
        orderList.add(dataOrder);

        model.addAttribute("allOrder", orderList);
        return "order-history";
    }

}
