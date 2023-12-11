package com.apapedia.catalogue.service;

import java.util.List;
import java.util.UUID;

import javax.xml.catalog.Catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.repository.CatalogueDb;

@Service
public class CatalogueService {
    @Autowired
    CatalogueDb catalogueDb;

    public void saveCatalogue(Catalogue catalogue) {
        catalogueDb.save(catalogue);
    }

    public List<Catalogue> getAllCatalogueByNameAsc() {
        return catalogueDb.findAllByOrderByProductNameLowerAsc();
    }

    public List<Catalogue> findAllCatalogues() {
        return catalogueDb.findAll();
    }

    public List<Catalogue> getAllCatalogueBySellerId(UUID sellerId) {
        return catalogueDb.findAllByIdSeller(sellerId);
    }

    public Catalogue getCatalogueById(UUID catalogId) {
        return catalogueDb.findById(catalogId).orElse(null);
    }

    public List<Catalogue> getCatalogListSorted(String sortBy, String order) {
        if (sortBy.equals("price") && order.equals("asc")) {
            return catalogueDb.findAllByOrderByPriceAsc();
        }

        if (sortBy.equals("price") && order.equals("desc")) {
            return catalogueDb.findAllByOrderByPriceDesc();
        }

        if (sortBy.equals("name") && order.equals("asc")) {
            return catalogueDb.findAllByOrderByProductNameLowerAsc();
        }

        if (sortBy.equals("name") && order.equals("desc")) {
            return catalogueDb.findAllByOrderByProductNameLowerDesc();
        }
        return null;
    }

    public List<Catalogue> getAllCatalogueByName(String name) {
        return catalogueDb.findAllByProductNameLowerContaining(name);
    }
}
