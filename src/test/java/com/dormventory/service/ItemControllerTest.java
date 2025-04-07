package com.dormventory.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dormventory.model.Item;
import com.dormventory.service.InventoryService;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private ItemController itemController;
    
    @Test
    void getMyItems_Success() {
        UUID userId = UUID.randomUUID();
        List<Item> expectedItems = Arrays.asList(new Item());
        when(inventoryService.getUserItems(userId)).thenReturn(expectedItems);
        
        ResponseEntity<List<Item>> response = itemController.getMyItems(userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
    }
    
    @Test
    void getRoomItems_Success() {
        UUID roomId = UUID.randomUUID();
        List<Item> expectedItems = Arrays.asList(new Item());
        when(inventoryService.getRoomItems(roomId)).thenReturn(expectedItems);
        
        ResponseEntity<List<Item>> response = itemController.getRoomItems(roomId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
    }
    
    @Test
    void addItem_Success() {
        UUID userId = UUID.randomUUID();
        Item item = new Item();
        when(inventoryService.addItem(item, userId)).thenReturn(item);
        
        ResponseEntity<Item> response = itemController.addItem(item, userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }
    
    @Test
    void deleteItem_Success() {
        UUID itemId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        
        ResponseEntity<Void> response = itemController.deleteItem(itemId, userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(inventoryService).deleteItem(itemId, userId);
    }
    
    @Test
    void transferItem_Success() {
        UUID itemId = UUID.randomUUID();
        UUID fromUserId = UUID.randomUUID();
        UUID toUserId = UUID.randomUUID();
        
        ResponseEntity<Void> response = itemController.transferItem(itemId, fromUserId, toUserId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(inventoryService).transferItem(itemId, fromUserId, toUserId);
    }
}
