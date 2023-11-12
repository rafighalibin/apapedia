package com.apapedia.catalogue.dto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalogue;


@Mapper(componentModel = "spring")
public interface CatalogueMapper {
    Catalogue createCatalogueRequestDTOToCatalogue(CreateCatalogueRequestDTO createCatalogueRequestDTO);
    @AfterMapping
    default void productNameLowerCreate(CreateCatalogueRequestDTO createCatalogueRequestDTO, @MappingTarget Catalogue catalogue){
        catalogue.setProductNameLower(createCatalogueRequestDTO.getProductName().toLowerCase());
    }
}
