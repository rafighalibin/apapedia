package com.apapedia.order.repository;


import com.apapedia.order.model.User;
import com.apapedia.order.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDb extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findById(UUID id);
}
