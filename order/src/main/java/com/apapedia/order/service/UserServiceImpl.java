package com.apapedia.order.service;

import com.apapedia.order.model.Order;
import com.apapedia.order.model.User;
import com.apapedia.order.model.UserModel;
import com.apapedia.order.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Override
    public UserModel findUserById(UUID uuid){
        Optional<UserModel> userOptional = userDb.findById(uuid);
        return userOptional.orElse(null);
    }

    @Override
    public UserModel updateSellerBalance(Order order){
        UserModel seller = findUserById(order.getSellerId());

        Long sellerCurrentBalance = seller.getBalance();
        seller.setUpdatedAt(LocalDateTime.now());
        seller.setBalance(sellerCurrentBalance + order.getTotalPrice());
        return userDb.save(seller);
    }
    @Override
    public UserModel updateBuyerBalance(Order order){
        UserModel buyer = findUserById(order.getCustomerId());
        Long buyerCurrentBalance = buyer.getBalance();

        buyer.setUpdatedAt(LocalDateTime.now());
        buyer.setBalance(buyerCurrentBalance - order.getTotalPrice());
        return userDb.save(buyer);
    }

}
