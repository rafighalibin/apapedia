package com.apapedia.frontend.DTO.response;


import com.apapedia.frontend.core.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryResponseDTO {
    List<Order> orderList;
}
