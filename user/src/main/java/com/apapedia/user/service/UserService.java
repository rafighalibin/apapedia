package com.apapedia.user.service;



import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User findUserById(String idString);

    User addUser(User user);

    User saveUser(User user);

    void deleteUser(String id);

    String encrypt(String password);

    User findUserByUsername(String username);

    User authenticate(String username, String password);

    String getJwtFromCookies(HttpServletRequest request);

    String getUsernameFromJwtCookie(HttpServletRequest request);

    void updateUser(UpdateUserRequestDTO user);

    boolean isLoggedIn(HttpServletRequest request);


}