package com.apapedia.frontend.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public ReadUserResponseDTO getUser() throws IOException, InterruptedException {

        JsonNode rootNode = getRequest("http://localhost:8081/api/user/");

        ReadUserResponseDTO user = new ReadUserResponseDTO();

        user.setId(UUID.fromString(rootNode.get("id").asText()));
        user.setName(rootNode.get("name").asText());
        user.setUsername(rootNode.get("username").asText());
        user.setEmail(rootNode.get("email").asText());
        user.setBalance(rootNode.get("balance").decimalValue());
        user.setAddress(rootNode.get("address").asText());

        user.setCategory("Official Store");

        return user;
    }

    public JsonNode getRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        return rootNode;
    }

}
