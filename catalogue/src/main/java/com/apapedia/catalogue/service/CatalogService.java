package com.apapedia.catalogue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.catalogue.model.Catalog;
import com.apapedia.catalogue.repository.CatalogDb;

@Service
public class CatalogService {
    @Autowired
    CatalogDb catalogDb;

    public void saveCatalog(Catalog catalog){
        catalogDb.save(catalog);
    }
}
