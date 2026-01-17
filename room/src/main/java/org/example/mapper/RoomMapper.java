package org.example.mapper;

import org.example.dto.RoomRequestDTO;
import org.example.dto.RoomResponseDTO;
import org.example.model.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponseDTO toDto(RoomEntity entity);

}
