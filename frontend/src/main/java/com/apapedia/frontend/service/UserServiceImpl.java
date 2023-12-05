package com.apapedia.frontend.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.HashMap;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
import com.apapedia.frontend.DTO.request.LoginRequestDTO;
import com.apapedia.frontend.DTO.request.TokenDTO;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

@Service
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Override
    public String getToken(String username, String name) {
        var body = new LoginRequestDTO(username, name);

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
    }
    @Override
    public ReadUserResponseDTO registerUser(CreateUserRequestDTO createUserDTO) throws IOException, InterruptedException {
        try {
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
            ReadUserResponseDTO userResponseDTO = new ReadUserResponseDTO();
            return userResponseDTO;
        }

    }

    @Override
    public ReadUserResponseDTO getUser(HttpServletRequest request) throws IOException, InterruptedException {
        JsonNode jsonResponse = requestToJSON(getRequest("http://localhost:8080/api/user/get", request));

        ReadUserResponseDTO user = new ReadUserResponseDTO();

        user.setId(UUID.fromString(jsonResponse.get("id").asText()));
        user.setName(jsonResponse.get("name").asText());
        user.setUsername(jsonResponse.get("username").asText());
        user.setEmail(user.getUsername()+"@ui.ac.id");
        user.setBalance(jsonResponse.get("balance").decimalValue());
        user.setAddress("Permata Puri 1");

        // TODO: get seller's category
        user.setCategory("Official Store");

        return user;
    }

    public HttpResponse<String> getRequest(String url, HttpServletRequest initialRequest)
            throws IOException, InterruptedException {

        // Method untuk melakukan GET request ke API

        String cookie = initialRequest != null ? getJwtFromCookies(initialRequest) : null;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .header("Cookie", "jwt=" + cookie)
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String> postRequest(String url, HttpServletRequest initialRequest,
            HashMap<String, String> postData)
            throws IOException, InterruptedException {

        // Method untuk melakukan POST request ke API

        String cookie = initialRequest != null ? getJwtFromCookies(initialRequest) : null;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(postData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Cookie", "jwt=" + cookie)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @Override
    public HttpResponse<String> login(String username, String password)
            throws IOException, InterruptedException {
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("password", password);
        postData.put("username", username);

        HttpServletRequest request = null;
        HttpResponse<String> response = postRequest("http://localhost:10140/api/user/authenticate", request, postData);

        return response;
    }

    @Override
    public void logout(HttpServletRequest request) throws IOException, InterruptedException{
        postRequest("http://localhost:10140/api/user/logout", request, null);
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

}
