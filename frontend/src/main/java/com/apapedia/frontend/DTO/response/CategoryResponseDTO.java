package com.apapedia.frontend.DTO.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponseDTO {
    private UUID id;
    private String name;
}
