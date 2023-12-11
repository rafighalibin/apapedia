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
import java.util.List;
import java.util.Map;

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

    @PostMapping("/cart/add-item/{idCart}")
    public ResponseEntity<CartItem> addItem(@Valid @RequestBody CreateCartItemRequestDTO cartItemDTO,
            @PathVariable("idCart") String idCart) {
        var cart = cartService.findCartById(UUID.fromString(idCart));
        var cartItem = cartMapper.createCartItemRequestDTOToCartItem(cartItemDTO);
        return ResponseEntity.ok(cartService.addItem(cart, cartItem));
    }

    @PutMapping("/cart/update-item/{idCart}")
    public ResponseEntity<CartItem> updateCartItem(@Valid @RequestBody UpdateCartItemRequestDTO cartItemDTO,
            @PathVariable("idCart") UUID idCart) {
        var cart = cartService.findCartById(idCart);
        var cartItemFromDto = cartMapper.updateCartItemRequestDTOToCartItem(cartItemDTO);
        var updatedCart = cartService.updateCartItem(cart, cartItemFromDto);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/cart/get/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getCartItems(@PathVariable("userId") UUID userId) {
        var cart = cartService.findCartByUserId(userId);
        List<CartItem> listCartItems = cart.getListCartItem();
        return ResponseEntity.ok(cartService.convertToJsonObject(listCartItems));
    }

    @GetMapping("/cart/delete-cart-items/{id}")
    public ResponseEntity<String> deleteCartItems(@PathVariable("id") UUID id) {
        cartService.deleteCartItems(id);
        return ResponseEntity.ok("Cart items has been deleted successfully");
    }
}
