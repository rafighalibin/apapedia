package com.apapedia.catalogue.service;

import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.repository.CatalogueDb;

@Service
public class CatalogueService {
    @Autowired
    CatalogueDb catalogueDb;

    public void saveCatalogue(Catalogue catalogue){
        catalogueDb.save(catalogue);
    }

    public List<Catalogue> getAllCatalogueByNameAsc(){
        return catalogueDb.findAllByOrderByProductNameLowerAsc();
    }

    public List<Catalogue> getAllCatalogueBySellerId(UUID sellerId) {
        return catalogueDb.findAllByIdSeller(sellerId);
    }

    public Catalogue getCatalogueById(UUID catalogId){
        return catalogueDb.findById(catalogId).orElse(null);
    }

}
