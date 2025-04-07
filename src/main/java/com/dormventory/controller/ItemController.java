package com.dormventory.controller;

import com.dormventory.model.Item;
import com.dormventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final InventoryService inventoryService;

    @GetMapping("/my-items")
    public ResponseEntity<List<Item>> getMyItems(@RequestParam UUID userId) {
        return ResponseEntity.ok(inventoryService.getUserItems(userId));
    }

    @GetMapping("/room-items")
    public ResponseEntity<List<Item>> getRoomItems(@RequestParam UUID roomId) {
        return ResponseEntity.ok(inventoryService.getRoomItems(roomId));
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item, @RequestParam UUID userId) {
        return ResponseEntity.ok(inventoryService.addItem(item, userId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId, @RequestParam UUID userId) {
        inventoryService.deleteItem(itemId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{itemId}/transfer")
    public ResponseEntity<Void> transferItem(
            @PathVariable UUID itemId,
            @RequestParam UUID fromUserId,
            @RequestParam UUID toUserId) {
        inventoryService.transferItem(itemId, fromUserId, toUserId);
        return ResponseEntity.ok().build();
    }
} 