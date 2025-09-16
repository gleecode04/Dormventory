package com.dormventory.controller;

import com.dormventory.model.Item;
import com.dormventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final InventoryService inventoryService;

    @GetMapping("/my-items")
    public ResponseEntity<List<Item>> getMyItems(Authentication authentication) {
        UUID userId = inventoryService.getCurrentUserId(authentication);
        return ResponseEntity.ok(inventoryService.getUserItems(userId));
    }

    @GetMapping("/room-items")
    public ResponseEntity<List<Item>> getRoomItems(@RequestParam UUID roomId) {
        return ResponseEntity.ok(inventoryService.getRoomItems(roomId));
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item, Authentication authentication) {
        UUID userId = inventoryService.getCurrentUserId(authentication);
        return ResponseEntity.ok(inventoryService.addItem(item, userId));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("@inventoryService.isItemOwner(#itemId, authentication.name)")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID itemId) {
        inventoryService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{itemId}/transfer")
    @PreAuthorize("@inventoryService.isItemOwner(#itemId, authentication.name)")
    public ResponseEntity<Void> transferItem(
            @PathVariable UUID itemId,
            @RequestParam UUID toUserId) {
        inventoryService.transferItem(itemId, toUserId);
        return ResponseEntity.ok().build();
    }
} 