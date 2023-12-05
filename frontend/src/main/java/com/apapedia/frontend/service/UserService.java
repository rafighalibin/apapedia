package com.apapedia.frontend.service;

import java.io.IOException;
import java.util.UUID;
import java.net.http.HttpResponse;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {
    ReadUserResponseDTO registerUser( CreateUserRequestDTO createUserDTO) throws IOException, InterruptedException;

    ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException;

    HttpResponse<String> login(String username, String password)
            throws IOException, InterruptedException;

    void logout( HttpServletRequest request) throws IOException, InterruptedException;

    String getToken(String username, String name);
}
