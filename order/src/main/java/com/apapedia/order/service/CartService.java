package com.apapedia.order.service;

import org.springframework.stereotype.Service;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;

@Service
public interface CartService {
    Cart createCart(Cart cart);

    CartItem addItem(CartItem cartItem);

    CartItem updateItem(CartItem cartItem);
}
