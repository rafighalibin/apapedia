package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apapedia.frontend.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CatalogController {

    @Autowired
    OrderService orderService;

    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        var graph =  orderService.getGraph(request);
        if (graph != null) model.addAttribute("activeNavbar", "Home");
        model.addAttribute("penjualanPerHari",  graph);
        return "home";
    }
    
}
