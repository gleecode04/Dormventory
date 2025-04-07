package com.dormventory.controller;

import com.dormventory.service.ReceiptService;
import com.dormventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;
    private final InventoryService inventoryService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadReceipt(
            @RequestParam("file") MultipartFile file) {
        try {
            List<String> extractedItems = receiptService.extractItemsFromReceipt(file);
            return ResponseEntity.ok(extractedItems);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmItems(
            @RequestBody List<String> confirmedItems,
            @RequestParam UUID userId) {
        try {
            for (String itemName : confirmedItems) {
                Item item = new Item();
                item.setName(itemName);
                inventoryService.addItem(item, userId);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 