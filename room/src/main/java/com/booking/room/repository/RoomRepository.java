package com.booking.room.repository;

import com.booking.room.model.RoomEntity;
import com.booking.room.enumerates.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findByType(RoomType type);
    List<RoomEntity> findByCapacityGreaterThanEqual(int capacity);
    List<RoomEntity> findByAvailableTrue();
}
