package com.apapedia.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBalanceAfterOrder {
    UUID sellerId;
    UUID customerId;
    long totalPrice;
}
