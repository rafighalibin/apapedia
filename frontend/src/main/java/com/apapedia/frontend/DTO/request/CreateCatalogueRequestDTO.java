package com.apapedia.frontend.DTO.request;

import java.util.UUID;

import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCatalogueRequestDTO {
    private String productName;
    private int price;
    private String productDescription;
    private int stock;
    private byte[] image;
    @JsonProperty("category")
    private CategoryResponseDTO categoryId;
    private UUID idSeller = UUID.randomUUID();
}
