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

import com.apapedia.user.model.Role;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    @Id
    private UUID id = UUID.randomUUID();

    
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    
    
    @Size(max = 100)
    @Column(name = "username")
    private String username;

    
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    
    @Size(max = 100)
    @Column(name = "email")
    private String email;


    
    @Size(max = 100)
    @Column(name = "address")
    private String address;

    long balance;


    private String role;

}