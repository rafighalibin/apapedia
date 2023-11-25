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
@Table(name = "cart")
public class Cart {

    @Id
    private UUID cartId = UUID.randomUUID();

    @Column(name = "id_user", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private int totalPrice;
}
