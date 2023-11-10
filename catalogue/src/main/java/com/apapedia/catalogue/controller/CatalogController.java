package com.apapedia.catalogue.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
