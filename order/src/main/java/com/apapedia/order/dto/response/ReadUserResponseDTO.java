package com.apapedia.order.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadUserResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private Long balance;
    private String address;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String category;
}
