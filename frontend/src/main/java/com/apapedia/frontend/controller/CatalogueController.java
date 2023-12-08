package com.apapedia.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;

@Controller
public class CatalogueController {
    @GetMapping("catalogue/add-product")
    public String formAddProduct(Model model) {
        //Membuat DTO baru sebagai isian form pengguna
        var catalgoueDTO = new CreateCatalogueRequestDTO();
        model.addAttribute("catalogueDTO", catalgoueDTO);

        return "form-add-product";
    }
}
