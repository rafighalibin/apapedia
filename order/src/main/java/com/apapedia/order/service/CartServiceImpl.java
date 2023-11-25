package com.apapedia.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    @Override
    public Cart createCart(Cart cart) {
        return cartDb.save(cart);
    }

    @Override
    public CartItem addItem(CartItem cartItem) {
        var cart = cartDb.findById(cartItem.getCart().getCartId()).get();
        return cart != null ? cartItemDb.save(cartItem) : null;
    }

    @Override 
    public CartItem updateItem(CartItem cartItem) {
        var cart = cartDb.findById(cartItem.getCart().getCartId()).get();
        if (cart != null) {
            
        }
    }

}
