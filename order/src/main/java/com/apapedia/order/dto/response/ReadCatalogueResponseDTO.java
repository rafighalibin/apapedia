package com.apapedia.order.dto.response;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadCatalogueResponseDTO {
    // TODO: DEBUG ONLY DELETE BEFORE COMMIT
    private UUID id;
    private UUID idSeller;
    private Integer price;
    private String productName;
    private String productDescription;
    private Map<String, Object> category;
    private Integer stock;
    private String image;
    private String productNameLower;
}
