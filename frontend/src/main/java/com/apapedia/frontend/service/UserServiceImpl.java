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
    public ReadUserResponseDTO getUserById(UUID id) throws IOException, InterruptedException {

        JsonNode rootNode = getRequest("https://ee523c5e-d3ce-4d13-9592-6db0ab0b6fcf.mock.pstmn.io/user/"
                + id.toString());

        ReadUserResponseDTO user = new ReadUserResponseDTO();

        user.setId(UUID.fromString(rootNode.get("id").asText()));
        user.setName(rootNode.get("name").asText());
        user.setUsername(rootNode.get("username").asText());
        user.setEmail(rootNode.get("email").asText());
        user.setBalance(rootNode.get("balance").decimalValue());
        user.setAddress(rootNode.get("address").asText());

        // TODO: might need to change this to correct format
        OffsetDateTime created_at = OffsetDateTime.parse(rootNode.get("created_at").asText());
        OffsetDateTime updated_at = OffsetDateTime.parse(rootNode.get("updated_at").asText());

        LocalDateTime localCreated_at = created_at.toLocalDateTime();
        LocalDateTime localUpdated_at = updated_at.toLocalDateTime();

        user.setCreated_at(localCreated_at);
        user.setUpdated_at(localUpdated_at);

        user.setCategory(rootNode.get("category").asText());

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
