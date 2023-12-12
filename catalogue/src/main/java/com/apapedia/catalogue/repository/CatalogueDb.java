package com.apapedia.catalogue.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apapedia.catalogue.model.Catalogue;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CatalogueDb extends JpaRepository<Catalogue, UUID> {
    List<Catalogue> findAllByOrderByProductNameLowerAsc();

    List<Catalogue> findAllByIdSeller(UUID sellerId);

    List<Catalogue> findAllByIdSellerOrderByProductNameLowerAsc(UUID sellerId);

    List<Catalogue> findAllByIdSellerOrderByPriceAsc(UUID idSeller);

    List<Catalogue> findAllByIdSellerOrderByPriceDesc(UUID idSeller);

    List<Catalogue> findAllByOrderByPriceAsc();

    List<Catalogue> findAll();

    List<Catalogue> findAllByOrderByPriceDesc();

    List<Catalogue> findAllByOrderByProductNameLowerDesc();
    
    List<Catalogue> findAllByProductNameLowerContaining(String name);

    List<Catalogue> findAllByIdSellerOrderByProductNameLowerDesc(UUID idSeller);
}
