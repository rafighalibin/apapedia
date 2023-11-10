package com.apapedia.catalogue.service;

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
}
