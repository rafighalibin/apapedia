package com.apapedia.order.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Cart;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface CartDb extends JpaRepository<Cart, UUID> {

}
