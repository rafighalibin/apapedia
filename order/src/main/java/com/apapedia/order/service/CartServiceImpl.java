package com.apapedia.order.service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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
        cart.setListCartItem(new ArrayList<CartItem>());
        return cartDb.save(cart);
    }

    @Override
    public CartItem addItem(Cart cart, CartItem cartItem) {
        if (cart != null) {
            cart.getListCartItem().add(cartItem);
            cartItem.setCart(cart);
            cartDb.save(cart);
            return cartItemDb.save(cartItem);
        }
        return null;
    }

    @Override 
    public CartItem updateCartItem(Cart cart, CartItem cartItemFromDto) {
        for (CartItem cartItem : cart.getListCartItem()){
            if (cartItem.getCartItemId().equals(cartItemFromDto.getCartItemId())){
                cartItem.setQuantity(cartItemFromDto.getQuantity());
                cartItemDb.save(cartItem);
                return cartItem;
            }
        }
        return null;
    }

    @Override
    public Cart findCartById(UUID idCart){
        return cartDb.findById(idCart).get();
    }

    @Override
    public Cart findCartByUserId(UUID idUser){
        return cartDb.findByUserId(idUser);
    }
    @Override
    public void deleteCartItems(UUID id){
        cartItemDb.deleteById(id);
    }

}
