package com.apapedia.catalogue.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.catalogue.dto.CatalogueMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.service.CatalogueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CatalogController {
    @Autowired
    CatalogueService catalogueService;

    @Autowired
    CatalogueMapper catalogueMapper;

    @PostMapping(value = "catalogue/create")
    public Catalogue tambahCatalog(@Valid @RequestBody CreateCatalogueRequestDTO catalogueDTO) {
        var catalog = catalogueMapper.createCatalogueRequestDTOToCatalogue(catalogueDTO);
        catalogueService.saveCatalogue(catalog);
        return catalog;
    }

    


    }
