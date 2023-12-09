package com.apapedia.frontend.service;

import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import java.util.*;
import reactor.core.publisher.Mono;

@Service
public interface CatalogueService {
    ReadCatalogueResponseDTO createCatalogue(CreateCatalogueRequestDTO catalogueDTO);
    List<CategoryResponseDTO> getAllCategory();
}
