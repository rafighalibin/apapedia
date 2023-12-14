package com.apapedia.frontend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserResponseDTO extends ReadUserResponseDTO {

    String password;
    String confirmPassword;

}
