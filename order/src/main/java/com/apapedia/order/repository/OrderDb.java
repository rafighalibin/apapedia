package com.apapedia.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Order;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface OrderDb extends JpaRepository<Order, UUID>{
    List<Order> findBySellerId(UUID sellerId);
}
