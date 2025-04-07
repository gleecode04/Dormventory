package com.dormventory.controller;

import com.dormventory.model.Room;
import com.dormventory.repository.RoomRepository;
import com.dormventory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable UUID id) {
        return ResponseEntity.ok(roomRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Room not found")));
    }
} 