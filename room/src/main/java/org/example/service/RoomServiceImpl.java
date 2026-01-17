package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.RoomRequestDTO;
import org.example.dto.RoomResponseDTO;
import org.example.enumerates.RoomType;
import org.example.exception.RangeIlegalException;
import org.example.mapper.RoomMapper;
import org.example.model.RoomEntity;
import org.example.repository.RoomRepository;
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

        // Get room type
        RoomType type = getTypeRoom(dto.getType());

        // Save room in DB
        RoomEntity entitySaved = roomRepository.save(RoomEntity.builder()
                .name(dto.getName())
                .capacity(dto.getCapacity())
                .type(type)
                .build());

        // Mapper to DTO
        RoomResponseDTO roomResponseDTO = mapper.toDto(entitySaved);

        return Optional.of(roomResponseDTO);
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> updateName(long id, String name) {
        return roomRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    roomRepository.save(entity);
                    RoomResponseDTO dto = mapper.toDto(entity);
                    return Optional.of(dto);
                }).orElse(null);
    }

    @Transactional
    @Override
    public Optional<RoomResponseDTO> updateAvailable(long id) {
        return roomRepository.findById(id)
                .map(entity -> {
                    entity.setAvailable(true);
                    roomRepository.save(entity);
                    RoomResponseDTO dto = mapper.toDto(entity);
                    return Optional.of(dto);
                }).orElse(null);
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

        if (n < 1 || n > 3) {
            throw new RangeIlegalException("El rango del tipo de sala debe estar comprendido entre 1 y 3");
        }

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
    public List<RoomResponseDTO> findAllAvailable(){
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

    private RoomType getTypeRoom(int n) {
        return RoomType.values()[n - 1];
    }
}
