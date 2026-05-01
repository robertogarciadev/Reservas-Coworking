package com.booking.room.controller;

import com.booking.room.dto.RoomRequestDTO;
import com.booking.room.dto.RoomResponseDTO;
import com.booking.room.service.IRoomService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/room")
public class RoomController {

    private final IRoomService roomService;

    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/save")
    public ResponseEntity<RoomResponseDTO> save(@RequestBody @Valid RoomRequestDTO dto) {
        return roomService.save(dto)
                .map(opt -> ResponseEntity.status(HttpStatus.CREATED).body(opt))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/update-name")
    public ResponseEntity<RoomResponseDTO> updateName(@RequestParam long id, String name) {
        return roomService.updateName(id, name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/update-available")
    public ResponseEntity<RoomResponseDTO> updateAvailable(@PathVariable long id) {
        return roomService.updateAvailable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/update-not-available")
    public ResponseEntity<RoomResponseDTO> updateNotAvailable(@PathVariable long id) {
        return roomService.updateNotAvailable(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/all-by-type")
    public ResponseEntity<List<RoomResponseDTO>> findByType(@PathVariable int type) {
        List<RoomResponseDTO> listRoom = roomService.findByType(type);
        if (!listRoom.isEmpty()) {
            return ResponseEntity.ok(listRoom);
        } else return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-by-capacity")
    public ResponseEntity<List<RoomResponseDTO>> findByCapacity(@PathVariable int capacity) {
        List<RoomResponseDTO> listRoom = roomService.findByCapacityMin(capacity);
        if (!listRoom.isEmpty()) {
            return ResponseEntity.ok(listRoom);
        } else return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-available")
    public ResponseEntity<List<RoomResponseDTO>> findAllAvailable() {
        List<RoomResponseDTO> listRoom = roomService.findAllAvailable();
        if (!listRoom.isEmpty()) {
            return ResponseEntity.ok(listRoom);
        } else return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<Map<String, String>> deleteById(@PathVariable long id) {
        Map<String, String> map = new HashMap<>();
        boolean deletedSuccess = roomService.deleteById(id);
        if (deletedSuccess) {
            map.put("message", "Room eliminado con exito");
            return ResponseEntity.accepted().body(map);
        } else {
            map.put("message", "Room no eliminado");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }
}
