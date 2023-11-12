package com.apapedia.user.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Size(max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", nullable = false)
    private String email;


    @NotNull
    @Size(max = 100)
    @Column(name = "address", nullable = false)
    private String address;

    long balance;


    private String role;

}