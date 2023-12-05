package com.apapedia.order.model;
import java.util.Date;
import java.util.UUID;
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
    private UUID orderId = UUID.randomUUID();

    @Column(name = "order_item_id", nullable = false)
    private UUID orderItemId;

    @NotNull
    @Column(name = "createdat", nullable = false)
    private Date createdAt;

    @NotNull
    @Column(name = "updatedat", nullable = false)
    private Date UpdatedAt;

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
    private List<OrderItem> listOrderItem;
}
