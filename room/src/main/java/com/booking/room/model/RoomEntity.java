package com.booking.room.model;

import com.booking.room.enumerates.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "room")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 100)
    private String name;
    private Integer capacity;
    @Enumerated (value = EnumType.STRING)
    private RoomType type;
    private Boolean available;

    @PrePersist
    void prePersist(){
        available=true;
    }
}
