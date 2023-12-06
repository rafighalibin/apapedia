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
    private Integer price;

    @NotNull
    @Column(name="product_name", nullable=false)
    private String productName;  

    @NotNull
    @Column(name="product_description", nullable=false)
    private String productDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category", referencedColumnName = "id_category")
    private Category category;

    @NotNull
    @Column(name="stock", nullable=false)
    private Integer stock;

    @Lob
    @Column(name="image", nullable=false)
    private byte[] image;

    @NotNull
    @Column(name = "product_name_lower")
    private String productNameLower;
}
