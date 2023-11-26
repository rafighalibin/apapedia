package com.apapedia.frontend.service;

import java.io.IOException;
import java.util.UUID;
import java.net.http.HttpResponse;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {
    ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException;

    HttpResponse<String> login(String username, String password)
            throws IOException, InterruptedException;

    void logout(HttpServletRequest request) throws IOException, InterruptedException;

    JsonNode updateUser(UpdateUserResponseDTO updateUserResponseDTO, HttpServletRequest request)
            throws IOException, InterruptedException;
}
