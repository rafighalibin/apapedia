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

    public void saveCatalogue(Catalogue catalogue) {
        catalogueDb.save(catalogue);
    }

    public List<Catalogue> getAllCatalogueByNameAsc() {
        return catalogueDb.findAllByOrderByProductNameLowerAsc();
    }

    public List<Catalogue> findAllCatalogues() {
        return catalogueDb.findAllByOrderByProductNameLowerAsc();
    }

    public List<Catalogue> getAllCatalogueBySellerId(UUID sellerId) {
        return catalogueDb.findAllByIdSellerOrderByProductNameLowerAsc(sellerId);
    }

    public Catalogue getCatalogueById(UUID catalogId) {
        return catalogueDb.findById(catalogId).orElse(null);
    }

    public List<Catalogue> getCatalogListSortedSeller(String sortBy, String order, UUID idSeller) {
        Boolean isAsc = order.equals("asc");
        Boolean byPrice = sortBy.equals("price");
        if (byPrice && isAsc) {
            return catalogueDb.findAllByIdSellerOrderByPriceAsc(idSeller);
        }

        if (byPrice) {
            return catalogueDb.findAllByIdSellerOrderByPriceDesc(idSeller);
        }

        if (isAsc) {
            return catalogueDb.findAllByIdSellerOrderByProductNameLowerAsc(idSeller);
        }

        return catalogueDb.findAllByIdSellerOrderByProductNameLowerDesc(idSeller);

    }

    public List<Catalogue> getCatalogListSorted(String sortBy, String order) {
        Boolean isAsc = order.equals("asc");
        Boolean byPrice = sortBy.equals("price");
        if (byPrice && isAsc) {
            return catalogueDb.findAllByOrderByPriceAsc();
        }

        if (byPrice) {
            return catalogueDb.findAllByOrderByPriceDesc();
        }

        if (isAsc) {
            return catalogueDb.findAllByOrderByProductNameLowerAsc();
        }

        return catalogueDb.findAllByOrderByProductNameLowerDesc();

    }

    public List<Catalogue> getAllCatalogueByName(String name) {
        return catalogueDb.findAllByProductNameLowerContaining(name);
    }
}
