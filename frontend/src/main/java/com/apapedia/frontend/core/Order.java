package com.apapedia.frontend.core;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
    private UUID id;
    private Date createdAt;
    private Date updatedAt;
    private int status;
    private int totalPrice;
    private UUID customer;
    private UUID seller;
    private List<OrderItem> orderItem;


    public Order(UUID id, Date createdAt, Date updatedAt, int status, int totalPrice, UUID customer, UUID seller) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.seller = seller;
    }
}
