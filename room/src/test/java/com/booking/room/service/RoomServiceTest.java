package com.booking.room.service;


import com.booking.room.dto.RoomRequestDTO;
import com.booking.room.dto.RoomResponseDTO;
import com.booking.room.enumerates.RoomType;
import com.booking.room.exception.RangeIlegalException;
import com.booking.room.mapper.RoomMapper;
import com.booking.room.model.RoomEntity;
import com.booking.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    RoomServiceImpl roomService;
    @Mock
    RoomRepository roomRepositoryMock;
    @Mock
    RoomMapper roomMapperMock;
    @Captor
    ArgumentCaptor<RoomEntity> roomEntityCaptor;

    @Test
    void saveRoom_shouldReturnOptional_whenRequestIsValid() {
        //Arrange
        RoomRequestDTO dtoRequestTest = getRoomRequestDTOTest();
        RoomEntity entityTest = getRoomEntityTest();
        RoomResponseDTO dtoResponseTest = RoomResponseDTO.builder()
                .id(entityTest.getId())
                .name(entityTest.getName())
                .capacity(entityTest.getCapacity())
                .type(entityTest.getType().toString())
                .available(entityTest.getAvailable())
                .build();

        when(roomRepositoryMock.save(any(RoomEntity.class))).thenReturn(entityTest);
        when(roomMapperMock.toDto(any(RoomEntity.class))).thenReturn(dtoResponseTest);

        //Act
        Optional<RoomResponseDTO> optRoomDTO = roomService.save(dtoRequestTest);

        //Assert
        assertTrue(optRoomDTO.isPresent());
        assertEquals(1L, entityTest.getId());
        verify(roomRepositoryMock, times(1)).save(roomEntityCaptor.capture());
        verify(roomMapperMock, times(1)).toDto(any(RoomEntity.class));

        RoomEntity captured = roomEntityCaptor.getValue();
        assertEquals(captured.getType(), entityTest.getType());
    }

    @Test
    void saveRoom_shouldThrowException_whenRangeIsNotValid() {
        //Arrange
        RoomRequestDTO dtoRequestTest = getRoomRequestDTOTest();
        dtoRequestTest.setType(0);

        //Act + assert
        assertThrows(RangeIlegalException.class,
                () -> roomService.save(dtoRequestTest));
        verify(roomRepositoryMock, never()).save(any(RoomEntity.class));
        verify(roomMapperMock, never()).toDto(any(RoomEntity.class));

    }


    @Test
    void updateName_shouldReturnOptional_whenRequestIsValid() {
        //Arrange
        long idTest = 1L;
        String nameTest = "Nombre cambiado";
        Optional<RoomEntity> optEntityTest = Optional.of(getRoomEntityTest());
        RoomResponseDTO dtoResponseTest = RoomResponseDTO.builder()
                .id(optEntityTest.get().getId())
                .name(nameTest)
                .capacity(optEntityTest.get().getCapacity())
                .type(optEntityTest.get().getType().toString())
                .available(optEntityTest.get().getAvailable())
                .build();
        when(roomRepositoryMock.findById(anyLong())).thenReturn(optEntityTest);
        when(roomRepositoryMock.save(any(RoomEntity.class))).thenReturn(optEntityTest.get());
        when(roomMapperMock.toDto(any(RoomEntity.class))).thenReturn(dtoResponseTest);

        //Act
        Optional<RoomResponseDTO> optRoomResDTO = roomService.updateName(idTest, nameTest);

        //Assert
        assertAll(
                () -> assertTrue(optRoomResDTO.isPresent()),
                () -> assertEquals(nameTest, optRoomResDTO.get().getName())
        );
        verify(roomRepositoryMock, times(1)).findById(anyLong());
        verify(roomRepositoryMock, times(1)).save(any(RoomEntity.class));
        verify(roomMapperMock, times(1)).toDto(any(RoomEntity.class));
    }

    @Test
    void updateName_shouldReturnOptEmpty_whenNotFoundEntity() {
        // Arrange
        long idTest = 1L;
        String nameTest = "Nombre cambiado";
        Optional<RoomEntity> optEmpty = Optional.empty();
        when(roomRepositoryMock.findById(anyLong())).thenReturn(optEmpty);
        // Act
        Optional<RoomResponseDTO> optEntity = roomService.updateName(idTest, nameTest);
        // Assert
        assertFalse(optEntity.isPresent());
        verify(roomRepositoryMock, times(1)).findById(anyLong());
        verify(roomRepositoryMock, never()).save(any(RoomEntity.class));
        verify(roomMapperMock, never()).toDto(any(RoomEntity.class));
    }

    @Test
    void updateAvailable_shouldReturnOptional_whenRequestIsValid() {
        //Arrange
        long idTest = 1L;
//        RoomEntity roomEntityTest = getRoomEntityTest();
//        roomEntityTest.setAvailable(false);

        Optional<RoomEntity> optTest = Optional.of(getRoomEntityTest());
        optTest.get().setAvailable(false);
        RoomResponseDTO roomResDtoTest = RoomResponseDTO.builder()
                .id(optTest.get().getId())
                .name(optTest.get().getName())
                .capacity(optTest.get().getCapacity())
                .available(optTest.get().getAvailable())
                .type(optTest.get().getType().toString()).build();
        when(roomRepositoryMock.findById(anyLong())).thenReturn(optTest);
        when(roomRepositoryMock.save(any(RoomEntity.class))).thenAnswer(invocation -> {
            RoomEntity e = invocation.getArgument(0);
            return RoomEntity.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .capacity(e.getCapacity())
                    .type(e.getType())
                    .available(e.getAvailable()).build();
        });
        when(roomMapperMock.toDto(any(RoomEntity.class))).thenAnswer();

        //Act
        Optional<RoomResponseDTO> dto = roomService.updateAvailable(idTest);

        //Assert
        assertAll(
                () -> assertTrue(dto.isPresent()),
                () -> assertTrue(dto.get().isAvailable())
        );
    }

    private RoomRequestDTO getRoomRequestDTOTest() {
        return RoomRequestDTO.builder()
                .type(1)
                .capacity(100)
                .name("Sala eventos")
                .build();
    }

    private RoomEntity getRoomEntityTest() {
        return RoomEntity.builder()
                .id(1L)
                .name("Sala eventos")
                .type(RoomType.MEETING_ROOM)
                .capacity(100)
                .available(true).build();
    }


}
