package com.dormventory.service;

import com.dormventory.model.Request;
import com.dormventory.model.User;
import com.dormventory.model.Room;
import com.dormventory.repository.RequestRepository;
import com.dormventory.repository.UserRepository;
import com.dormventory.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private InventoryService inventoryService;
    
    @InjectMocks
    private RequestService requestService;
    
    private User sender;
    private Room room;
    private Request request;
    private UUID senderId;
    
    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        room = new Room();
        room.setId(UUID.randomUUID());
        
        sender = new User();
        sender.setId(senderId);
        sender.setRoom(room);
        
        request = new Request();
        request.setId(UUID.randomUUID());
        request.setSender(sender);
        request.setRoom(room);
        request.setContent("Need ketchup");
    }
    
    @Test
    void getRoomRequests_Success() {
        UUID roomId = room.getId();
        List<Request> expectedRequests = Arrays.asList(request);
        when(requestRepository.findByRoomIdOrderByCreatedAtDesc(roomId))
            .thenReturn(expectedRequests);
        
        List<Request> result = requestService.getRoomRequests(roomId);
        
        assertEquals(expectedRequests, result);
    }
    
    @Test
    void createRequest_Success() {
        String content = "Need ketchup";
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        
        Request result = requestService.createRequest(content, senderId);
        
        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(sender, result.getSender());
        assertEquals(room, result.getRoom());
    }
    
    @Test
    void createRequest_UserNotFound_ThrowsException() {
        when(userRepository.findById(senderId)).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () ->
            requestService.createRequest("Need ketchup", senderId));
    }
    
    @Test
    void fulfillRequest_Success() {
        UUID itemId = UUID.randomUUID();
        UUID fulfillerId = UUID.randomUUID();
        
        when(requestRepository.findById(request.getId()))
            .thenReturn(Optional.of(request));
        
        requestService.fulfillRequest(request.getId(), itemId, fulfillerId);
        
        verify(inventoryService).transferItem(itemId, fulfillerId, senderId);
        verify(requestRepository).save(request);
        assertTrue(request.isFulfilled());
    }
    
    @Test
    void fulfillRequest_RequestNotFound_ThrowsException() {
        UUID requestId = UUID.randomUUID();
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () ->
            requestService.fulfillRequest(requestId, UUID.randomUUID(), UUID.randomUUID()));
    }
} 