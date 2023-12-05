package com.apapedia.catalogue.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apapedia.catalogue.model.Catalog;

import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface CatalogDb extends JpaRepository<Catalog, UUID> {
    @Query("SELECT c FROM Catalog c ORDER BY c.productName")
    List<Catalog> sortCatalogByNameLower();
    List<Catalog> findAllByOrderByProductNameAsc();
    List<Catalog> findAllByIdSeller(UUID sellerId);
}
