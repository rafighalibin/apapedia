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
            var listCatalogue =  this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
            for (var catalogue : listCatalogue){
                catalogue.setImageString(Base64.getEncoder().encodeToString(catalogue.getImage()));
            }
            return listCatalogue;

        } else{
            String url = "/api/catalogue/viewall";

            var listCatalogue = this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
            for (var catalogue : listCatalogue){
                catalogue.setImageString(Base64.getEncoder().encodeToString(catalogue.getImage()));
            }
            return listCatalogue;
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
    public ReadCatalogueResponseDTO getCatalogueById(UUID id, HttpServletRequest request) {
        String url = "/api/catalogue/"+id;
        return this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToMono(ReadCatalogueResponseDTO.class).block();
    }

    @Override
    public ReadCatalogueResponseDTO updateCatalogue(UpdateCatalogueResponseDTO updateCatalogueResponseDTO, HttpServletRequest request){
            var response = this.webClient
                .put()
                .uri("/api/catalogue/update/"+updateCatalogueResponseDTO.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateCatalogueResponseDTO)
                .retrieve()
                .bodyToMono(ReadCatalogueResponseDTO.class)
                .block();

        return response;
    }

    @Override
    public List<ReadCatalogueResponseDTO> listCatalogueFiltered(String productName, HttpServletRequest request){
        String url = "/api/catalogue/search?query="+productName;
        var token = getJwtFromCookies(request);
        try {
            var listCatalogue = this.webClient.get().uri(url).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
            
            for (var catalogue : listCatalogue){
                if (token != null && catalogue.getIdSeller().equals(UUID.fromString(getIdFromJwtToken(token))) || token == null){
                    catalogue.setImageString(Base64.getEncoder().encodeToString(catalogue.getImage()));
                } else {
                    listCatalogue.remove(catalogue);
                }
            } 
            return listCatalogue;
        } catch (Exception e){
            return null;
        }
    }


    @Override
    public List<ReadCatalogueResponseDTO> getCatalogueListSorted(String sortBy, String order, HttpServletRequest request){
        String url = "/api/catalogue/filter?sortBy="+sortBy+"&order="+order;
        var listCatalogue = this.webClient.get().uri(url).header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request)).retrieve().bodyToFlux(ReadCatalogueResponseDTO.class).collectList().block();
        for (var catalogue : listCatalogue){
            catalogue.setImageString(Base64.getEncoder().encodeToString(catalogue.getImage()));
        }
        return listCatalogue;

    }
}
