package com.apapedia.catalogue.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCatalogueRequestDTO {
    private String productName;
    @PositiveOrZero
    private int price;
    private String productDescription;
    private int stock;
    private byte[] image;
    private UUID idCategory;
    private UUID idSeller;
}
