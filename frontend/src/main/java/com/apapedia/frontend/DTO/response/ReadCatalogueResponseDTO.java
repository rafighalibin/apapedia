package com.apapedia.frontend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadCatalogueResponseDTO {
    private UUID id;
    private Integer price;
    private String productName;  
    private String productDescription;
    private CategoryResponseDTO category;
    private Integer stock;
    private byte[] image;
    private String imageString;
}
