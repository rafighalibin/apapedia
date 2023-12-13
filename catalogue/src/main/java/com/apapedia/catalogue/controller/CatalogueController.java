package com.apapedia.catalogue.controller;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.apapedia.catalogue.security.jwt.JwtUtils;
import com.apapedia.catalogue.service.CatalogueService;
import com.apapedia.catalogue.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class CatalogueController {
    @Autowired
    CatalogueService catalogueService;

    @Autowired
    CatalogueMapper catalogueMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(value = "catalogue/create")
    public ResponseEntity<?> tambahCatalog(@Valid @RequestBody CreateCatalogueRequestDTO catalogueDTO, HttpServletRequest request) {
        var idSeller = jwtUtils.getIdFromJwtToken(jwtUtils.parseJwt(request));
        var idSellerUUID = UUID.fromString(idSeller);
        catalogueDTO.setIdSeller(idSellerUUID);
        var catalog = catalogueMapper.createCatalogueRequestDTOToCatalogue(catalogueDTO);
        catalogueService.saveCatalogue(catalog);

        return ResponseEntity.ok(catalog);
    }

    @GetMapping(value = "/catalogue/viewall")
    public ResponseEntity<List<Catalogue>> getAllCatalogue() {
        List<Catalogue> catalogues = catalogueService.findAllCatalogues();
        return ResponseEntity.ok(catalogues);
    }

    @GetMapping(value = "/catalogue")
    public  ResponseEntity<List<Catalogue>> getAllCatalogSortedByName() {
       List<Catalogue> catalogues = catalogueService.getAllCatalogueByNameAsc();
       return ResponseEntity.ok(catalogues);
    }

    @GetMapping(value = "/catalogue/by-seller/{sellerId}")
    public ResponseEntity<List<Catalogue>> getAllCatalogueBySellerId(@PathVariable("sellerId") UUID sellerId) {
        List<Catalogue> catalogues = catalogueService.getAllCatalogueBySellerId(sellerId);
        return ResponseEntity.ok(catalogues);
    }

    @GetMapping(value = "/catalogue/{catalogueId}")
    public ResponseEntity<Catalogue> getCatalogueById(@PathVariable("catalogueId") UUID catalogId) {
        Catalogue catalogue = catalogueService.getCatalogueById(catalogId);
        return ResponseEntity.ok(catalogue);
    }

    @GetMapping(value = "/catalogue/search")
    public ResponseEntity<List<Catalogue>> getAllCatalogueByName(@RequestParam(value="query") String name) {
        List<Catalogue> catalogues = catalogueService.getAllCatalogueByName(name.toLowerCase());
        if (catalogues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catalogues);
    }


    @PutMapping(value="/catalogue/update/{catalogueId}")
    public ResponseEntity<Catalogue> updateCatalogue(@PathVariable("catalogueId") UUID catalogId, @Valid @RequestBody UpdateCatalogueRequestDTO updateCatalogueRequestDTO, HttpServletRequest request) {
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

    if (updateCatalogueRequestDTO.getCategory() != null) {
        catalogue.setCategory(updateCatalogueRequestDTO.getCategory());
    }
        
        catalogueService.saveCatalogue(catalogue);
        return ResponseEntity.ok(catalogue);
    }
    

    @GetMapping(value = "/catalogue/filter", params = { "sortBy", "order" })
    public ResponseEntity<List<Catalogue>> getCatalogListSorted(
            @RequestParam("sortBy") String sortBy,
            @RequestParam("order") String order, HttpServletRequest request) {


        if (!(sortBy.equals("price") || sortBy.equals("name")) || !(order.equals("asc") || order.equals("desc"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            var idSeller = jwtUtils.getIdFromJwtToken(jwtUtils.parseJwt(request));
            List<Catalogue> catalogues = catalogueService.getCatalogListSortedSeller(sortBy, order, UUID.fromString(idSeller));
            return ResponseEntity.ok(catalogues);
        } catch (Exception e) {
            List<Catalogue> catalogues = catalogueService.getCatalogListSorted(sortBy, order);
            return ResponseEntity.ok(catalogues);
        }
    }

}
