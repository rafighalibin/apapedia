package com.apapedia.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.CartItem;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CartItemDb extends JpaRepository<CartItem, UUID> {

}
