package com.apapedia.order.dto.request;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderRequestDTO {
    private UUID orderItemId;
    private Date createdAt;
    private Date UpdatedAt;
    private int status;
    private int totalPrice;
    private UUID customerId;
    private UUID sellerId;
}
