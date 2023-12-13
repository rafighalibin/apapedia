package com.apapedia.frontend.DTO.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCatalogueResponseDTO {
    private UUID id;
    @PositiveOrZero(message = "Product price must be 0 or greater")
    private Integer price;
    @NotBlank(message = "Product name cannot be blank")
    private String productName;
    @NotBlank(message = "Product Description cannot be blank")
    private String productDescription;
    @JsonProperty("category")
    private CategoryResponseDTO categoryId;
    @PositiveOrZero(message = "Product stock must be 0 or greater")
    private Integer stock;
    private byte[] image;

    @JsonIgnore
    private MultipartFile imageFile;
    
}
