package com.apapedia.user.repository;

import com.apapedia.user.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserDb extends JpaRepository<User, UUID>{

    User findByUsername(String username);

    Optional<User> findById(UUID id);


}

