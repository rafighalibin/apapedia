package com.apapedia.order.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartItemRequestDTO {

    private UUID productId;

    @NotNull
    private int quantity;
}
