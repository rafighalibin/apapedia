package com.apapedia.frontend.service;

import java.io.IOException;
import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {
        ReadUserResponseDTO registerUser(CreateUserRequestDTO createUserDTO) throws IOException, InterruptedException;

        ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException;

        String getToken(String username, String name);

        String updateUser(UpdateUserResponseDTO updateUserResponseDTO, HttpServletRequest request)
                        throws IOException, InterruptedException;

        ReadUserResponseDTO withdrawBalance(HttpServletRequest request, Long amount);

        String deleteUser(HttpServletRequest request)
            throws IOException, InterruptedException;
}
