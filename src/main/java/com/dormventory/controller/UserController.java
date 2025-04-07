package com.dormventory.controller;

import com.dormventory.model.User;
import com.dormventory.repository.UserRepository;
import com.dormventory.repository.RoomRepository;
import com.dormventory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getRoom() != null && user.getRoom().getId() != null) {
            user.setRoom(roomRepository.findById(user.getRoom().getId())
                .orElseThrow(() -> new NotFoundException("Room not found")));
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found")));
    }
} 