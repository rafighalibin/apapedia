package com.apapedia.user.repository;

import com.apapedia.user.model.*;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserDb extends JpaRepository<UserModel, UUID>{

    UserModel findByUsername(String username);

    Optional<UserModel> findById(UUID id);


}

