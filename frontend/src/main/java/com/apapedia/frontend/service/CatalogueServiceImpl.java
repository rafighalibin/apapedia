package com.apapedia.frontend.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.apapedia.frontend.DTO.request.CreateCatalogueRequestDTO;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.response.ReadCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateCatalogueResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import com.apapedia.frontend.DTO.response.GraphResponseDTO;

import reactor.core.publisher.Mono;
import java.util.*;
@Service
public class CatalogueServiceImpl implements CatalogueService {
   private final WebClient webClient;

   private String jwtSecret = "apapedia21";

   public CatalogueServiceImpl(WebClient.Builder webClientBuilder) {
       this.webClient = webClientBuilder.baseUrl("http://localhost:10142").build();
   }

   @Override
   public ReadCatalogueResponseDTO createCatalogue(CreateCatalogueRequestDTO catalogueDTO, HttpServletRequest request) {
       var response = webClient.post()
           .uri("/api/catalogue/create")
           .contentType(MediaType.APPLICATION_JSON)
           .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
           .bodyValue(catalogueDTO)
           .retrieve()
           .bodyToMono(ReadCatalogueResponseDTO.class);
        var catalogueCreated  = response.block();
        return catalogueCreated;
   }


    @Override
   public List<CategoryResponseDTO> getAllCategory(HttpServletRequest request) {
        String url = "/api/category/all";
        return this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(CategoryResponseDTO.class).collectList().block();
    }

    @Override
    public List<ReadCatalogueResponseDTO> getAllCatalogue(HttpServletRequest request) {
        var token = getJwtFromCookies(request);
        if (token != null){
            String sellerId = getIdFromJwtToken(token);
            String url = "/api/catalogue/by-seller/"+ sellerId;
        return this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
        } else{
            String url = "/api/catalogue/viewall";
        return this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
        }
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getIdFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", String.class);
        } catch (ExpiredJwtException ex) {
            // Handle token expiration
            System.out.println("Token has expired.");
        } catch (JwtException ex) {
            // Handle other JWT-related exceptions
            System.out.println("Error parsing JWT: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ReadCatalogueResponseDTO getCatalogueById(UUID id) {
        String url = "/api/category/"+id;
        return this.webClient.get().uri(url).retrieve().bodyToMono(ReadCatalogueResponseDTO.class).block();
    }

    @Override
    public ReadCatalogueResponseDTO updateCatalogue(UpdateCatalogueResponseDTO updateCatalogueResponseDTO){
            var response = this.webClient
                .put()
                .uri("/api/catalogue/update/"+updateCatalogueResponseDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCatalogueResponseDTO)
                .retrieve()
                .bodyToMono(ReadCatalogueResponseDTO.class)
                .block();

        return response;
    }
    
}
