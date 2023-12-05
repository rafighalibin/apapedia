package com.apapedia.catalogue.controller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

import javax.xml.catalog.Catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.catalogue.dto.CatalogueMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.dto.request.UpdateCatalogueRequestDTO;
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

    @GetMapping(value = "/catalogue/{catalogueId}")
    public ResponseEntity<Catalogue> getCatalogueById(@PathVariable("catalogueId") UUID catalogId) {
        Catalogue catalogue = catalogueService.getCatalogueById(catalogId);
        if (catalogue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catalogue);
    }

    @GetMapping(value = "/catalogue", params = "name")
    public ResponseEntity<List<Catalogue>> getAllCatalogueByName(@RequestParam("name") String name) {
        List<Catalogue> catalogues = catalogueService.getAllCatalogueByName(name.toLowerCase());
        if (catalogues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catalogues);
    }
}

    @PutMapping(value="/catalogue/{catalogueId}")
    public ResponseEntity<Catalogue> updateCatalogue(@PathVariable("catalogueId") UUID catalogId, @Valid @RequestBody UpdateCatalogueRequestDTO updateCatalogueRequestDTO) {
        // Retrieve the existing catalog from the database
        Catalogue catalogue = catalogueService.getCatalogueById(catalogId);
        if (catalogue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // Update fields only if they are not null in the request
    if (updateCatalogueRequestDTO.getProductName() != null) {
        catalogue.setProductName(updateCatalogueRequestDTO.getProductName());
    }

    if (updateCatalogueRequestDTO.getProductName() != null) {
        catalogue.setProductNameLower(updateCatalogueRequestDTO.getProductName().toLowerCase());
    }

    if (updateCatalogueRequestDTO.getPrice() != null) {
        catalogue.setPrice(updateCatalogueRequestDTO.getPrice());
    }

    if (updateCatalogueRequestDTO.getProductDescription() != null) {
        catalogue.setProductDescription(updateCatalogueRequestDTO.getProductDescription());
    }

    if (updateCatalogueRequestDTO.getStock() != null) {
        catalogue.setStock(updateCatalogueRequestDTO.getStock());
    }

    if (updateCatalogueRequestDTO.getImage() != null) {
        catalogue.setImage(updateCatalogueRequestDTO.getImage());
    }

    if (updateCatalogueRequestDTO.getIdCategory() != null) {
        catalogue.setIdCategory(updateCatalogueRequestDTO.getIdCategory());
    }
        
        catalogueService.saveCatalogue(catalogue);
        return ResponseEntity.ok(catalogue);
    }
    

    }
