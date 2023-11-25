package com.apapedia.frontend.core;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItem {
    private UUID productId;
    private UUID orderId;
    private int quantity;
    private String productName;
    private int productPrice;
    
    public OrderItem(UUID productId, UUID orderId, int quantity, String productName, int productPrice) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        
    }
}
