package com.apapedia.user.dto.response;

import java.util.List;

import com.apapedia.user.dto.request.CreateCartRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCartResponseDTO extends CreateCartRequestDTO {
    private List<CartItemResponseDTO> listCartItem;
}
