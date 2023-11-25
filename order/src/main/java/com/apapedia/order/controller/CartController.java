package com.apapedia.order.controller;

import org.springframework.web.bind.annotation.RestController;

import com.apapedia.order.dto.CartMapper;
import com.apapedia.order.dto.request.CreateCartItemRequestDTO;
import com.apapedia.order.dto.request.CreateCartRequestDTO;
import com.apapedia.order.dto.request.UpdateCartItemRequestDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.service.CartService;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/cart/update")
    public CartItem updateItem(@Valid @RequestBody UpdateCartItemRequestDTO cartItemDTO) {
        var cartItem = cartMapper.updateCartItemRequestDTOToCartItem(cartItemDTO);
        return cartService.addItem(cartItem);
    }

    @GetMapping("/cart/delete-cart-items/{id}")
    public ResponseEntity<String> deleteCartItems(@PathVariable("id") UUID id){
        cartService.deleteCartItems(id);
        return ResponseEntity.ok("Cart items has been deleted successfully");
    }
}
