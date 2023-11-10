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

import com.apapedia.catalogue.dto.CatalogMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.service.CatalogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CatalogController {
    @Autowired
    CatalogService catalogService;

    @Autowired
    CatalogMapper catalogMapper;

    @PostMapping(value = "catalog/create")
    public Catalog tambahCatalog(@Valid @RequestBody CreateCatalogRequestDTO catalogDTO) {
        var catalog = catalogMapper.createCatalogRequestDTOToCatalog(catalogDTO);
        catalogService.saveCatalog(catalog);
        return catalog;
    }

    


    }
