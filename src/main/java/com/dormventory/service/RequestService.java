package com.dormventory.service;

import com.dormventory.model.Request;
import com.dormventory.model.User;
import com.dormventory.repository.RequestRepository;
import com.dormventory.repository.UserRepository;
import com.dormventory.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    public List<Request> getRoomRequests(UUID roomId) {
        return requestRepository.findByRoomIdOrderByCreatedAtDesc(roomId);
    }

    @Transactional
    public Request createRequest(String content, UUID senderId) {
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        Request request = new Request();
        request.setContent(content);
        request.setSender(sender);
        request.setRoom(sender.getRoom());

        return requestRepository.save(request);
    }

    @Transactional
    public void fulfillRequest(UUID requestId, UUID itemId, UUID fulfillerId) {
        Request request = requestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Request not found"));

        // Transfer item to requester
        inventoryService.transferItem(itemId, fulfillerId, request.getSender().getId());

        // Mark request as fulfilled
        request.setFulfilled(true);
        requestRepository.save(request);
    }
} 