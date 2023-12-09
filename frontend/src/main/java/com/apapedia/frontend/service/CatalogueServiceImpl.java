package com.apapedia.frontend.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import reactor.core.publisher.Mono;
import java.util.*;
@Service
public class CatalogueServiceImpl implements CatalogueService {
   private final WebClient webClient;

   public CatalogueServiceImpl(WebClient.Builder webClientBuilder) {
       this.webClient = webClientBuilder.baseUrl("https://apap-141.cs.ui.ac.id").build();
   }

   public ReadCatalogueResponseDTO createCatalogue(CreateCatalogueRequestDTO catalogueDTO) {
       var response = webClient.post()
           .uri("/api/catalogue/create")
           .contentType(MediaType.APPLICATION_JSON)
           .bodyValue(catalogueDTO)
           .retrieve()
           .bodyToMono(ReadCatalogueResponseDTO.class);
        var catalogueCreated  = response.block();
        return catalogueCreated;
   }

   public List<CategoryResponseDTO> getAllCategory() {
        String url = "/api/category/all";
        return this.webClient.get().uri(url).retrieve().bodyToFlux(CategoryResponseDTO.class).collectList().block();
    }

    public List<ReadCatalogueResponseDTO> getAllCatalogue() {
        String url = "/api/catalogue/viewall";
        return this.webClient.get().uri(url).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
    }
}
