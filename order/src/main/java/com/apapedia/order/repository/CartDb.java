package com.apapedia.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Cart;

import jakarta.transaction.Transactional;

@Repository
public interface CartDb extends JpaRepository<Cart, UUID> {
    Cart findByCartId(UUID id);

    Cart findByUserId(UUID userId);
}
