package com.booking.room.mapper;


import com.booking.room.dto.RoomResponseDTO;
import com.booking.room.model.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponseDTO toDto(RoomEntity entity);

}
