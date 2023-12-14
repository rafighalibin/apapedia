package com.apapedia.frontend.service;

import java.io.IOException;
import java.net.http.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.request.DeleteUserRequestDTO;
import com.apapedia.frontend.DTO.request.LoginRequestDTO;
import com.apapedia.frontend.DTO.request.TokenDTO;
import com.apapedia.frontend.DTO.request.UpdateBalanceRequestDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

@Service
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    private String jwtSecret = "apapedia21";

    public UserServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apap-140.cs.ui.ac.id/")
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

    public String getIdFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", String.class);
        } catch (ExpiredJwtException ex) {
            // Handle token expiration
            System.out.println("Token has expired.");
        } catch (JwtException ex) {
            // Handle other JWT-related exceptions
            System.out.println("Error parsing JWT: " + ex.getMessage());
        }
        return null;
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

    @Override
    public String deleteUser(HttpServletRequest request)
            throws IOException, InterruptedException {
        var idUser = getIdFromJwtToken(getJwtFromCookies(request));
        var idUserDTO = new DeleteUserRequestDTO();
        idUserDTO.setId(idUser);

        var response = this.webClient
                .put()
                .uri("/api/user/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtFromCookies(request))
                .header("Cookie", "jwt=" + getJwtFromCookies(request))
                .bodyValue(idUserDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

}
