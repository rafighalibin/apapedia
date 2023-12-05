package com.apapedia.user.service;

import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.LoginJwtRequestDTO;
import com.apapedia.user.dto.request.UpdateBalance;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.*;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    String login(AuthenticationRequest authenticationRequest);

    String loginSeller(LoginJwtRequestDTO loginJwtRequestDTO);

    UserModel findUserById(String idString);

    UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO);

    UserModel saveUser(UserModel user);

    void deleteUser(String id);

    String encrypt(String password);

    UserModel findUserByUsername(String username);

    String getJwtFromCookies(HttpServletRequest request);

    String getUsernameFromJwtCookie(HttpServletRequest request);

    void updateUser(HttpServletRequest request, UpdateUserRequestDTO newUser);

    boolean isLoggedIn(HttpServletRequest request);

    UserModel updateBalance(HttpServletRequest request, UpdateBalance newBalance);

    String checkUsernameEmailPassword(HttpServletRequest request, UpdateUserRequestDTO newUser);

    String extractJwtFromRequest(HttpServletRequest request);

}