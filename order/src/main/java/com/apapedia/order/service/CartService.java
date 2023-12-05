package com.apapedia.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;

@Service
public interface CartService {
    Cart createCart(Cart cart);

    CartItem addItem(Cart cart, CartItem cartItem);

    CartItem updateCartItem(Cart cart, CartItem cartItem);

    Cart findCartById(UUID idCart);

    Cart findCartByUserId(UUID idUser);
    
    void deleteCartItems(UUID id);
}
