package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.RoomRequestDTO;
import org.example.dto.RoomResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IRoomService {

    Optional<RoomResponseDTO> save(RoomRequestDTO room);

    @Transactional
    Optional<RoomResponseDTO> updateName(long id, String name);

    @Transactional
    Optional<RoomResponseDTO> updateAvailable(long id);

    @Transactional
    Optional<RoomResponseDTO> updateNotAvailable(long id);

    List<RoomResponseDTO> findByType(int n);

    List<RoomResponseDTO> findByCapacityMin(int capacity);

    List<RoomResponseDTO> findAllAvailable();

    @Transactional
    boolean deleteById(long id);
}
