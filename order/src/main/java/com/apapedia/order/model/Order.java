package com.apapedia.order.model;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @Column(name = "order_id")
    private UUID orderId = UUID.randomUUID();

    @NotNull
    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updatedat", nullable = false)
    private LocalDateTime UpdatedAt;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @NotNull
    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> listOrderItem;
}
