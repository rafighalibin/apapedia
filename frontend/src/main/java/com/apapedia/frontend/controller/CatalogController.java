package com.apapedia.frontend.controller;

import org.glassfish.jaxb.runtime.v2.schemagen.xmlschema.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.service.CatalogueService;
import com.apapedia.frontend.service.OrderService;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CatalogController {

    @Autowired
    OrderService orderService;
    @Autowired
    CatalogueService catalogueService;

    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        var graph =  orderService.getGraph(request);
        if (graph != null) model.addAttribute("activeNavbar", "Home");
        model.addAttribute("penjualanPerHari",  graph);
        model.addAttribute("listCatalogue", catalogueService.getAllCatalogue());
        return "home";
    }
    
    @GetMapping("/catalogue/create")
    public String formAddProduct(Model model) {
        //Membuat DTO baru sebagai isian form pengguna
        var catalgoueDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogueDTO", catalgoueDTO);
        model.addAttribute("listCategory", catalogueService.getAllCategory());
        return "form-add-product";
    }

    @PostMapping("/catalogue/create")
    public String addProduct(@ModelAttribute CreateCatalogueRequestDTO catalogueDTO){
        catalogueService.createCatalogue(catalogueDTO);
        return "redirect:/home";
    }

}
