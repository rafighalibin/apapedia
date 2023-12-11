package com.apapedia.frontend.service;

import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateCatalogueResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@Service
public interface CatalogueService {
    ReadCatalogueResponseDTO createCatalogue(CreateCatalogueRequestDTO catalogueDTO, HttpServletRequest request);
    List<CategoryResponseDTO> getAllCategory(HttpServletRequest request);
    List<ReadCatalogueResponseDTO> getAllCatalogue(HttpServletRequest request);
    ReadCatalogueResponseDTO getCatalogueById(UUID id);
    ReadCatalogueResponseDTO updateCatalogue(UpdateCatalogueResponseDTO catalogue);
    public String getJwtFromCookies(HttpServletRequest request);
}
