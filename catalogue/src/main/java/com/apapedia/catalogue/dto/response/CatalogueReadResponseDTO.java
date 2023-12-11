package com.apapedia.catalogue.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogueReadResponseDTO {

    private UUID id;
    private UUID idSeller;
    private Integer price;
    private String productName;
    private String productDescription;
    private String categoryName;
    private Integer stock;
}
