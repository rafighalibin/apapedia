package com.apapedia.frontend.service;

import java.io.IOException;
import java.net.http.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.request.LoginRequestDTO;
import com.apapedia.frontend.DTO.request.TokenDTO;
import com.apapedia.frontend.DTO.request.UpdateBalanceRequestDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

@Service
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:10140")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String getToken(String username, String name) {
        var body = new LoginRequestDTO(username, name);

        try {

        var response = this.webClient
                .post()
                .uri("/api/login/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TokenDTO.class)
                .block();

        var token = response.getToken();

        return token;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ReadUserResponseDTO registerUser(CreateUserRequestDTO createUserDTO)
            throws IOException, InterruptedException {
        try {
            createUserDTO.setPassword("ariefthegoat");
            createUserDTO.setRole("Seller");
            createUserDTO.setEmail(createUserDTO.getUsername() + "@ui.ac.id");
            var response = this.webClient
                    .post()
                    .uri("/api/user/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(createUserDTO)
                    .retrieve()
                    .bodyToMono(ReadUserResponseDTO.class);
            var userSubmitted = response.block();
            return userSubmitted;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException {
        var user = this.webClient
                .get()
                .uri("/api/user/get")
                .header("Cookie", "jwt=" + getJwtFromCookies(request))
                .retrieve()
                .bodyToMono(ReadUserResponseDTO.class);

        ReadUserResponseDTO userResponseDTO = user.block();

        return userResponseDTO;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public JsonNode requestToJSON(HttpResponse<String> response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response.body());

        return jsonResponse;
    }

    @Override
    public String updateUser(UpdateUserResponseDTO updateUserResponseDTO, HttpServletRequest request)
            throws IOException, InterruptedException {
        var response = this.webClient
                .put()
                .uri("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                .header("Cookie", "jwt=" + getJwtFromCookies(request))
                .bodyValue(updateUserResponseDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

    @Override
    public ReadUserResponseDTO withdrawBalance(HttpServletRequest request, Long amount) {
        ReadUserResponseDTO user = new ReadUserResponseDTO();
        try {
            user = getUser(request);
            Long newBalance = user.getBalance() - amount;
            user.setBalance(newBalance);

            UpdateBalanceRequestDTO data = new UpdateBalanceRequestDTO();
            data.setBalance(user.getBalance());

            var response = this.webClient
                    .put()
                    .uri("/api/user/update-balance")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                    .header("Cookie", "jwt=" + getJwtFromCookies(request))
                    .bodyValue(data)
                    .retrieve()
                    .bodyToMono(UpdateBalanceRequestDTO.class)
                    .block();
            // TODO: add error handling
            return user;

        } catch (Exception e) {
            return null;
        }
    }

}
