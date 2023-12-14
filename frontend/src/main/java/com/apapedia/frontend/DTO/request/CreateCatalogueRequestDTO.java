package com.apapedia.frontend.DTO.request;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.apapedia.frontend.DTO.response.CategoryResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCatalogueRequestDTO {
    @NotBlank(message = "Product name cannot be blank")
    private String productName;
    @PositiveOrZero(message = "Product price must be 0 or greater")
    private int price;
    @NotBlank(message = "Product Description cannot be blank")
    private String productDescription;
    @PositiveOrZero(message = "Product stock must be 0 or greater")
    private int stock;
    private byte[] image;
    @JsonIgnore
    private MultipartFile imageFile;
    @JsonProperty("category")
    private CategoryResponseDTO categoryId;
    private UUID idSeller;
}
