package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.apapedia.frontend.service.CatalogueService;
import com.apapedia.frontend.service.OrderService;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;

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
        var token = catalogueService.getJwtFromCookies(request);
        if(token != null) model.addAttribute("isLoggedIn", "True");
        model.addAttribute("penjualanPerHari",  graph);
        model.addAttribute("listCatalogue", catalogueService.getAllCatalogue(request));
        
        return "home";
    }
    
    @GetMapping("/catalogue/create")
    public String formAddProduct(Model model, HttpServletRequest request ) {
        //Membuat DTO baru sebagai isian form pengguna
        var catalgoueDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogueDTO", catalgoueDTO);
        model.addAttribute("listCategory", catalogueService.getAllCategory(request));
        return "form-add-product";
    }

    @PostMapping("/catalogue/create")
    public String addProduct(@ModelAttribute CreateCatalogueRequestDTO catalogueDTO, HttpServletRequest request){
        catalogueService.createCatalogue(catalogueDTO,request);
        return "redirect:/home";
    }

    @GetMapping("/catalogue/{id}/update")
    public String formUbahCatalogue(@PathVariable("id") UUID id, Model model, HttpServletRequest request) {
        //Mengambil catalogue dengan id tersebut
        ReadCatalogueResponseDTO catalogue = catalogueService.getCatalogueById(id,request);
        UpdateCatalogueResponseDTO catalogueDTO = new UpdateCatalogueResponseDTO();
        catalogueDTO.setId(catalogue.getId());
        catalogueDTO.setPrice(catalogue.getPrice());
        catalogueDTO.setProductName(catalogue.getProductName());
        catalogueDTO.setProductDescription(catalogue.getProductDescription());
        catalogueDTO.setCategoryId(catalogue.getCategory());
        catalogueDTO.setStock(catalogue.getStock());
        catalogueDTO.setImage(catalogue.getImage());

        model.addAttribute("catalogueDTO", catalogueDTO);
        model.addAttribute("listCategory", catalogueService.getAllCategory(request));

        return "form-edit-product";
    }

    @PostMapping("/catalogue/{id}/update")
    public String UbahCatalogue(@ModelAttribute UpdateCatalogueResponseDTO updateCatalogueResponseDTO, HttpServletRequest request) {
        catalogueService.updateCatalogue(updateCatalogueResponseDTO, request);
        return "redirect:/home";
    }

    @GetMapping("/catalogue/{id}/update")
    public String formUbahCatalogue(@PathVariable("id") UUID id, Model model) {
        //Mengambil catalogue dengan id tersebut
        ReadCatalogueResponseDTO catalogue = catalogueService.getCatalogueById(id);
        UpdateCatalogueResponseDTO catalogueDTO = new UpdateCatalogueResponseDTO();
        catalogueDTO.setId(catalogue.getId());
        catalogueDTO.setPrice(catalogue.getPrice());
        catalogueDTO.setProductName(catalogue.getProductName());
        catalogueDTO.setProductDescription(catalogue.getProductDescription());
        catalogueDTO.setCategory(catalogue.getCategory());
        catalogueDTO.setStock(catalogue.getStock());
        catalogueDTO.setImage(catalogue.getImage());

        model.addAttribute("catalogueDTO", catalogueDTO);

        return "form-edit-product";
    }

    @PostMapping("/catalgue/{id}/update")
    public String UbahCatalogue(@ModelAttribute UpdateCatalogueResponseDTO updateCatalogueResponseDTO) {
        catalogueService.updateCatalogue(updateCatalogueResponseDTO);
        return "home";
    }

}
