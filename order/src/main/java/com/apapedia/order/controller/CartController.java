package com.apapedia.order.controller;

import org.springframework.web.bind.annotation.RestController;

import com.apapedia.order.dto.CartMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.request.CreateCartRequestDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.service.CartService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @PostMapping("/cart/create")
    public Cart createCart(@Valid @RequestBody CreateCartRequestDTO cartDTO) {
        var cart = cartMapper.createCartRequestDTOToCart(cartDTO);
        return cartService.createCart(cart);
    }

    @PostMapping("/cart/add-item")
    public CartItem addItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemDTO) {
        var cartItem = cartMapper.createCartItemRequestDTOToCartItem(cartItemDTO);
        return cartService.addItem(cartItem);
    }

}
