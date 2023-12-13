package com.apapedia.user.service;

import com.apapedia.user.dto.request.*;
import com.apapedia.user.dto.response.UpdateUserBalanceResponse;
import com.apapedia.user.model.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String login(AuthenticationRequest authenticationRequest);

    String loginSeller(LoginJwtRequestDTO loginJwtRequestDTO);

    UserModel findUserById(String idString);

    UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO);

    UserModel saveUser(UserModel user);

    void deleteUser(String id);

    void updateUserDeleted(UserModel user, CreateUserRequestDTO createUserRequestDTO);

    String encrypt(String password);

    UserModel findUserByUsername(String username);

    String getJwtFromHeader(HttpServletRequest request);

    String getUsernameFromJwtCookie(HttpServletRequest request);

    UserModel updateUser(HttpServletRequest request, UpdateUserRequestDTO newUser);

    boolean isLoggedIn(HttpServletRequest request);

    UserModel updateBalance(HttpServletRequest request, UpdateBalance newBalance);

    UpdateUserBalanceResponse updateBalanceAfterTransaction(UpdateBalanceAfterOrder newBalance);

    String checkUsernameEmailPassword(HttpServletRequest request, UpdateUserRequestDTO newUser);

    String extractJwtFromRequest(HttpServletRequest request);

}