package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apapedia.frontend.service.OrderService;

@Controller
public class CatalogController {

    @Autowired
    OrderService orderService;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("penjualanPerHari", orderService.getGraph());
        return "home";
    }
    
}
