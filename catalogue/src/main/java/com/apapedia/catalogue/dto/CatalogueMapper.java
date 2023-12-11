package com.apapedia.catalogue.dto;

import org.hibernate.validator.constraints.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.model.Category;


@Mapper(componentModel = "spring")
public interface CatalogueMapper {
    Catalogue createCatalogueRequestDTOToCatalogue(CreateCatalogueRequestDTO createCatalogueRequestDTO);
    @AfterMapping
    default void productNameLowerCreate(CreateCatalogueRequestDTO createCatalogueRequestDTO, @MappingTarget Catalogue catalogue){
        catalogue.setProductNameLower(createCatalogueRequestDTO.getProductName().toLowerCase());
    }



}
