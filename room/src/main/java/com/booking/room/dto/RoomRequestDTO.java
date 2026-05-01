package com.booking.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomRequestDTO {

    @NotBlank(message = "El nombre de la sale no puede estar vacío")
    private String name;
    @Min(value = 1, message = "El espacio debe ser mayor a 0")
    private int capacity;
    @Range(min = 1, max = 3, message = "El rango de la sala debe estar comprendido entre 1 y 3")
    private int type;
}
