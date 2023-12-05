package com.apapedia.catalogue.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Collectors;

import javax.xml.catalog.Catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class CatalogueController {
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

    @GetMapping(value = "/catalogue")
    public List<Catalogue> getAllCatalogSortedByName() {
        return catalogueService.getAllCatalogueByNameAsc();
    }

    @GetMapping(value = "/catalogue/by-seller/{sellerId}")
    public ResponseEntity<List<Catalogue>> getAllCatalogueBySellerId(@PathVariable("sellerId") UUID sellerId) {
        List<Catalogue> catalogues = catalogueService.getAllCatalogueBySellerId(sellerId);
        if (catalogues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catalogues);
    }

    @GetMapping(value="/catalogue/{catalogueId}")
    public ResponseEntity<Catalogue> getCatalogueById(@PathVariable("catalogueId") UUID catalogId) {
        Catalogue catalogue = catalogueService.getCatalogueById(catalogId);
        if (catalogue == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catalogue);
    }
    

    }
