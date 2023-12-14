package com.apapedia.catalogue.dto.request;

import com.apapedia.catalogue.model.Category;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCatalogueRequestDTO {
    private String productName;
    @PositiveOrZero
    private Integer price;
    private String productDescription;
    private Integer stock;
    private byte[] image;
    private Category category;

}