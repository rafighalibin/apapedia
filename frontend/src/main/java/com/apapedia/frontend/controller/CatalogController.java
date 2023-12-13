package com.apapedia.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateCatalogueResponseDTO;
import com.apapedia.frontend.service.CatalogueService;
import com.apapedia.frontend.service.OrderService;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class CatalogController {

    @Autowired
    OrderService orderService;
    @Autowired
    CatalogueService catalogueService;

    @GetMapping("/")
    public String homePage(Model model, @RequestParam(value = "query", required = false) String productName, HttpServletRequest request) {
        var graph =  orderService.getGraph(request);
        if (graph != null) model.addAttribute("activeNavbar", "LoggedIn");
        else model.addAttribute("activeNavbar", "NotLoggedIn");
        var token = catalogueService.getJwtFromCookies(request);
        if (token != null) model.addAttribute("isLoggedIn", "True");
        model.addAttribute("penjualanPerHari",  graph);

        List<ReadCatalogueResponseDTO> listCatalogue;
        if (productName != null){
            listCatalogue = catalogueService.listCatalogueFiltered(productName,request);
        } else {
            listCatalogue = catalogueService.getAllCatalogue(request);
        }

        model.addAttribute("listCatalogue", listCatalogue);
        
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
    public String addProduct(@Valid @ModelAttribute CreateCatalogueRequestDTO catalogueDTO, BindingResult bindingResult, HttpServletRequest request, Model model) throws Exception{
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> {
                        if (error instanceof FieldError) {
                            FieldError fieldError = (FieldError) error;
                            return fieldError.getField() + ": " + error.getDefaultMessage();
                        }
                        return error.getDefaultMessage();
                    })
                    .collect(Collectors.toList());

            model.addAttribute("errors", errors);
            return "error-view";
        }
        catalogueDTO.setImage(catalogueDTO.getImageFile().getBytes());
        catalogueService.createCatalogue(catalogueDTO,request);
        return "redirect:/";
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
    public String UbahCatalogue(@Valid @ModelAttribute UpdateCatalogueResponseDTO updateCatalogueResponseDTO, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> {
                        if (error instanceof FieldError) {
                            FieldError fieldError = (FieldError) error;
                            return fieldError.getField() + ": " + error.getDefaultMessage();
                        }
                        return error.getDefaultMessage();
                    })
                    .collect(Collectors.toList());

            model.addAttribute("errors", errors);
            return "error-view";
        }
        updateCatalogueResponseDTO.setImage(updateCatalogueResponseDTO.getImageFile().getBytes());
        catalogueService.updateCatalogue(updateCatalogueResponseDTO, request);
        return "redirect:/";
    }

    @GetMapping("/catalogue/{id}")
    public String detailCatalogue(@PathVariable("id") UUID id, Model model, HttpServletRequest request) throws Exception{
        ReadCatalogueResponseDTO catalogueDTO = catalogueService.getCatalogueById(id, request);
        catalogueDTO.setImageString(Base64.getEncoder().encodeToString(catalogueDTO.getImage()));
        var token = catalogueService.getJwtFromCookies(request);
        if (token != null) model.addAttribute("isLoggedIn", "True");
        model.addAttribute("catalogueDTO", catalogueDTO);
        return "catalogue-view";
    }

    @GetMapping("/catalogue/filter")
    public String catalogueListSorted(@RequestParam(name = "sortBy", required = false) String sortBy, @RequestParam(name = "order", required = false) String order,
                                    Model model, HttpServletRequest request){
        List<ReadCatalogueResponseDTO> listCatalogue = catalogueService.getCatalogueListSorted(sortBy, order, request);
        for (ReadCatalogueResponseDTO c : listCatalogue) {
            c.setImageString(Base64.getEncoder().encodeToString(c.getImage()));
          }
        model.addAttribute("listCatalogue", listCatalogue);                                
        return "home";                          
    }
}
