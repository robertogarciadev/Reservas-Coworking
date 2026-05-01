package com.booking.room.service;

import com.booking.room.dto.RoomRequestDTO;
import com.booking.room.dto.RoomResponseDTO;
import com.booking.room.enumerates.RoomType;
import com.booking.room.exception.RangeIlegalException;
import com.booking.room.mapper.RoomMapper;
import com.booking.room.model.RoomEntity;
import com.booking.room.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper mapper;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper mapper) {
        this.roomRepository = roomRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> save(RoomRequestDTO dto) {

        // Checkout range
        rangeIsValid(dto.getType());

        // Get room type
        RoomType type = getTypeRoom(dto.getType());

        // Save room in DB
        RoomEntity saved = roomRepository.save(RoomEntity.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .type(type)
                .build());

        // Mapper to DTO
        RoomResponseDTO roomResponseDTO = mapper.toDto(saved);

        return Optional.of(roomResponseDTO);
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> updateName(long id, String name) {
        return roomRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    RoomEntity roomUpdated = roomRepository.save(entity);
                    RoomResponseDTO dto = mapper.toDto(roomUpdated);
                    return Optional.of(dto);
                }).orElse(Optional.empty());
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> updateAvailable(long id) {
        return roomRepository.findById(id)
                .map(entity -> {
                    entity.setAvailable(true);
                    RoomEntity entityUpdated = roomRepository.save(entity);
                    RoomResponseDTO dto = mapper.toDto(entityUpdated);
                    return Optional.of(dto);
                }).orElse(Optional.empty());
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> updateNotAvailable(long id) {
        return roomRepository.findById(id)
                .map(entity -> {
                    entity.setAvailable(false);
                    roomRepository.save(entity);
                    RoomResponseDTO dto = mapper.toDto(entity);
                    return Optional.of(dto);
                }).orElse(null);
    }

    @Override
    public List<RoomResponseDTO> findByType(int n) {

        // Comprueba si el rango es válido, si no, lanza excepcion
        rangeIsValid(n);

        // Get room type
        RoomType type = getTypeRoom(n);

        return roomRepository.findByType(type).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<RoomResponseDTO> findByCapacityMin(int capacity) {
        return roomRepository.findByCapacityGreaterThanEqual(capacity).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<RoomResponseDTO> findAllAvailable() {
        return roomRepository.findByAvailableTrue().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        return roomRepository.findById(id)
                .map(entity -> {
                    roomRepository.deleteById(entity.getId());
                    return true;
                }).orElse(false);
    }

    private void rangeIsValid(int n) {
        if (n < 1 || n > 3) {
            throw new RangeIlegalException("El rango del tipo de sala debe estar comprendido entre 1 y 3");
        }
    }

    private RoomType getTypeRoom(int n) {
        return RoomType.values()[n - 1];
    }
}
