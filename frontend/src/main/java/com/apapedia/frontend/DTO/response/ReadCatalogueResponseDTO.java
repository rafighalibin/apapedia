package com.apapedia.frontend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadCatalogueResponseDTO {
    private Integer price;
    private String productName;  
    private String productDescription;
    private CategoryResponseDTO category;
    private Integer stock;
    private byte[] image;
}
