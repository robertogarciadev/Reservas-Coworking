package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomResponseDTO {
    private long id;
    private String name;
    private int capacity;
    private String type;
    private boolean available;
}
