package com.apapedia.frontend.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String name;

    @NotBlank(message = "Username tidak boleh kosong")
    private String username;

    private String password;

    private String email;

    @NotBlank(message = "Address tidak boleh kosong")
    private String address;

    private String category;

    long balance;

    private String role;

}