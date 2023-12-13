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
    ReadCatalogueResponseDTO getCatalogueById(UUID id, HttpServletRequest request);
    ReadCatalogueResponseDTO updateCatalogue(UpdateCatalogueResponseDTO catalogue, HttpServletRequest request);
    public String getJwtFromCookies(HttpServletRequest request);
    List<ReadCatalogueResponseDTO> listCatalogueFiltered(String productName, HttpServletRequest request);
    List<ReadCatalogueResponseDTO> getCatalogueListSorted(String sortBy, String order, HttpServletRequest request);
}
