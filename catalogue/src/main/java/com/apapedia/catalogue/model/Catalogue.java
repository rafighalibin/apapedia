package com.apapedia.catalogue.model;


import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogue")
public class Catalogue {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name="id_seller", nullable=false)
    private UUID idSeller;

    @NotNull
    @Column(name="price", nullable=false)
    private int price;

    @NotNull
    @Column(name="product_name", nullable=false)
    private String productName;  

    @NotNull
    @Column(name="product_description", nullable=false)
    private String productDescription;

    @NotNull
    @Column(name="id_category", nullable=false)
    private UUID idCategory;

    @NotNull
    @Column(name="stock", nullable=false)
    private int stock;

    @Column(name="image", nullable=false)
    private String image;
}
