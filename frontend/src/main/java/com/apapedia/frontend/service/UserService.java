package com.apapedia.frontend.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;

@Service
public interface UserService {
    ReadUserResponseDTO getUserById(UUID id) throws IOException, InterruptedException;
}
