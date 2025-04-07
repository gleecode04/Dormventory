package com.dormventory.service;

import com.dormventory.model.Item;
import com.dormventory.model.User;
import com.dormventory.model.Room;
import com.dormventory.repository.ItemRepository;
import com.dormventory.repository.UserRepository;
import com.dormventory.exception.NotFoundException;
import com.dormventory.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private ItemRepository itemRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private InventoryService inventoryService;
    
    private User owner;
    private Room room;
    private Item item;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        room = new Room();
        room.setId(UUID.randomUUID());
        
        owner = new User();
        owner.setId(userId);
        owner.setRoom(room);
        
        item = new Item();
        item.setId(UUID.randomUUID());
        item.setOwner(owner);
        item.setRoom(room);
    }
    
    @Test
    void addItem_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        
        Item result = inventoryService.addItem(item, userId);
        
        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        assertEquals(room, result.getRoom());
    }
    
    @Test
    void deleteItem_UnauthorizedUser_ThrowsException() {
        UUID differentUserId = UUID.randomUUID();
        
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        
        assertThrows(UnauthorizedException.class, () -> 
            inventoryService.deleteItem(item.getId(), differentUserId)
        );
    }

    @Test
    void getRoomItems_Success() {
        UUID roomId = room.getId();
        List<Item> expectedItems = Arrays.asList(item);
        when(itemRepository.findByRoomId(roomId)).thenReturn(expectedItems);
        
        List<Item> result = inventoryService.getRoomItems(roomId);
        
        assertEquals(expectedItems, result);
    }

    @Test
    void getUserItems_Success() {
        List<Item> expectedItems = Arrays.asList(item);
        when(itemRepository.findByOwnerId(userId)).thenReturn(expectedItems);
        
        List<Item> result = inventoryService.getUserItems(userId);
        
        assertEquals(expectedItems, result);
    }

    @Test
    void transferItem_Success() {
        UUID toUserId = UUID.randomUUID();
        User newOwner = new User();
        newOwner.setId(toUserId);
        
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(newOwner));
        
        inventoryService.transferItem(item.getId(), userId, toUserId);
        
        assertEquals(newOwner, item.getOwner());
        verify(itemRepository).save(item);
    }

    @Test
    void deleteItem_Success() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        
        inventoryService.deleteItem(item.getId(), userId);
        
        verify(itemRepository).delete(item);
    }
} 