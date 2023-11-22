package com.apapedia.order.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartRequestDTO {
    private UUID userId;

    @PositiveOrZero
    private int totalPrice;
}
