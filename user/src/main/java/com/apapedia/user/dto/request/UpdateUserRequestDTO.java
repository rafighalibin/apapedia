package com.apapedia.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserRequestDTO extends CreateUserRequestDTO {
    private UUID id;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
