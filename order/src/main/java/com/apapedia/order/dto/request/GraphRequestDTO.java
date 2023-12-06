package com.apapedia.order.dto.request;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GraphRequestDTO {
   HashMap<Integer, Integer> graph;
}
