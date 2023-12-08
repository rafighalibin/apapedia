package com.apapedia.frontend.DTO.request;

import java.util.UUID;

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
    private UUID idCategory;
    private UUID idSeller;
}
