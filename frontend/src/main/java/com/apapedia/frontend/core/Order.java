package com.apapedia.frontend.core;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
    private UUID orderId;
    private Date createdAt;
    private Date updatedAt;
    private int status;
    private int totalPrice;
    private UUID customerId;
    private UUID sellerId;
    private List<OrderItem> orderItem;


    public Order(UUID orderId, Date createdAt, Date updatedAt, int status, int totalPrice, UUID customerId, UUID sellerId) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
        this.sellerId = sellerId;
    }
}
