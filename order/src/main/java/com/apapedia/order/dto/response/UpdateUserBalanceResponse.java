package com.apapedia.order.dto.response;

import java.util.List;
import java.util.UUID;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.User;
import com.apapedia.order.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserBalanceResponse {
    private Order order;
    private UserModel buyer;
    private UserModel seller;
}