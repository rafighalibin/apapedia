package com.apapedia.user.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;


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