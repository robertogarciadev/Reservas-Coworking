package org.example.repository;

import org.example.enumerates.RoomType;
import org.example.model.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findByType(RoomType type);
    List<RoomEntity> findByCapacityGreaterThanEqual(int capacity);
    List<RoomEntity> findByAvailableTrue();
}
