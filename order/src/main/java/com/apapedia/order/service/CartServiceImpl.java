package com.apapedia.order.service;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apapedia.order.dto.response.ReadCatalogueResponseDTO;
import com.apapedia.order.model.Cart;
import com.apapedia.order.model.CartItem;
import com.apapedia.order.repository.CartDb;
import com.apapedia.order.repository.CartItemDb;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartDb cartDb;

    @Autowired
    CartItemDb cartItemDb;

    private final WebClient webClient;

    public CartServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apap-142.cs.ui.ac.id")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Cart createCart(Cart cart) {
        Cart cartExisting = cartDb.findByUserId(cart.getUserId());
        if (cartExisting != null && cartExisting.getListCartItem().size() > 0) {
            return cartExisting;
        }
        cart.setListCartItem(new ArrayList<CartItem>());

        // TODO: DEBUG ONLY DELETE BEFORE COMMIT
        // ParameterizedTypeReference<List<ReadCatalogueResponseDTO>> responseType = new
        // ParameterizedTypeReference<List<ReadCatalogueResponseDTO>>() {
        // };

        // var response = this.webClient
        // .get()
        // .uri("/api/catalogue")
        // .retrieve().toEntity(responseType)
        // .block();
        // cartDb.save(cart);
        // for (ReadCatalogueResponseDTO e : response.getBody()) {
        // CartItem cartItem = new CartItem();
        // cartItem.setCart(cart);
        // cartItem.setProductId(e.getId());
        // cartItem.setQuantity(1);
        // cart.getListCartItem().add(cartItem);
        // cartItemDb.save(cartItem);
        // }
        return cartDb.save(cart);
    }

    public JsonNode requestToJSON(HttpResponse<String> response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());

        return jsonResponse;
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
        for (CartItem cartItem : cart.getListCartItem()) {
            if (cartItem.getCartItemId().equals(cartItemFromDto.getCartItemId())) {
                cartItem.setQuantity(cartItemFromDto.getQuantity());
                cartItemDb.save(cartItem);
                return cartItem;
            }
        }
        return null;
    }

    @Override
    public Cart findCartById(UUID idCart) {
        return cartDb.findById(idCart).get();
    }

    @Override
    public Cart findCartByUserId(UUID idUser) {
        return cartDb.findByUserId(idUser);
    }

    @Override
    public void deleteCartItems(UUID id) {
        cartItemDb.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> convertToJsonObject(List<CartItem> listCartItems) {
        // TODO: convert all CartItem to map
        ParameterizedTypeReference<ReadCatalogueResponseDTO> responseType = new ParameterizedTypeReference<ReadCatalogueResponseDTO>() {
        };
        List<Map<String, Object>> result = new ArrayList<>();
        for (CartItem cartItem : listCartItems) {
            Map<String, Object> subResult = new HashMap<>();
            UUID itemId = cartItem.getProductId();
            var response = this.webClient
                    .get()
                    .uri("/api/catalogue/" + itemId.toString())
                    .retrieve()
                    .toEntity(responseType)
                    .block();
            subResult.put("cartItemId", cartItem.getCartItemId());
            subResult.put("product", response.getBody());
            subResult.put("quantity", cartItem.getQuantity());
            result.add(subResult);
        }
        return result;
    }

}
