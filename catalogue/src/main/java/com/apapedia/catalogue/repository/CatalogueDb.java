package com.apapedia.catalogue.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.catalogue.model.Catalogue;

import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface CatalogueDb extends JpaRepository<Catalogue, UUID> {
    List<Catalogue> findAllByOrderByProductNameLowerAsc();
    List<Catalogue> findAllByIdSeller(UUID sellerId);
}
