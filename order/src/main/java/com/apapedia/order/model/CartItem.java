package com.apapedia.order.model;

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
@Table(name = "cart_item")
public class CartItem {

    @Id
    private UUID cartItemId = UUID.randomUUID();

    @Column(name = "id_product", nullable = false)
    private UUID productId;

    @Column(name = "id_cart", nullable = false)
    private UUID cartId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private int quantity;
}
