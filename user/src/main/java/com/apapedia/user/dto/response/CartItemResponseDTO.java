package com.apapedia.user.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemResponseDTO {
    private UUID cartItemId;

    private UUID productId;

    private int quantity;
}
