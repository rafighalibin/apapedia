package com.apapedia.catalogue.dto;

import org.mapstruct.Mapper;

import com.apapedia.catalogue.dto.request.CreateCatalogRequestDTO;
import com.apapedia.catalogue.model.Catalog;


@Mapper(componentModel = "spring")
public interface CatalogMapper {
    Catalog createCatalogRequestDTOToCatalog(CreateCatalogRequestDTO createCatalogRequestDTO);
}
