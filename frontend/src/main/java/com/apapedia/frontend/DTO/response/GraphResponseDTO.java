package com.apapedia.frontend.DTO.response;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GraphResponseDTO {
    HashMap<Integer, Integer> graph;
}
