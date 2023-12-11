package com.apapedia.order.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(value = {"order"}, allowSetters = true)
@Table(name = "order_item")
public class OrderItem {

    @Id
    private UUID orderItemId = UUID.randomUUID();

    @Column(name = "id_product", nullable = false)
    private UUID productId;
    
    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "order_id")
    private Order order;

    @NotNull
    @Column(name = "quanitity", nullable = false)
    private int quantity;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "product_price", nullable = false)
    private int productPrice;

}
