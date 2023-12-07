package com.apapedia.order.service;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.User;
import com.apapedia.order.model.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserModel findUserById(UUID uuid);

    UserModel updateBuyerBalance(Order order);
    UserModel updateSellerBalance(Order order);
}