package com.apapedia.frontend.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    private String username;
    private String password;

    // Getters and setters
}