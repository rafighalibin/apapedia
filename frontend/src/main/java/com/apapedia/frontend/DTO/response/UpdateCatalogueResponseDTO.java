package com.apapedia.frontend.DTO.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCatalogueResponseDTO {
    private UUID id;
    private Integer price;
    private String productName;  
    private String productDescription;
    @JsonProperty("category")
    private CategoryResponseDTO categoryId;
    private Integer stock;
    private byte[] image;
}