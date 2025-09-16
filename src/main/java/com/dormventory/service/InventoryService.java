package com.dormventory.service;

import com.dormventory.model.Item;
import com.dormventory.model.User;
import com.dormventory.repository.ItemRepository;
import com.dormventory.repository.UserRepository;
import com.dormventory.exception.NotFoundException;
import com.dormventory.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<Item> getUserItems(UUID userId) {
        return itemRepository.findByOwnerId(userId);
    }

    public List<Item> getRoomItems(UUID roomId) {
        return itemRepository.findByRoomId(roomId);
    }

    @Transactional
    public Item addItem(Item item, UUID userId) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        
        item.setOwner(owner);
        item.setRoom(owner.getRoom());
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        itemRepository.delete(item);
    }

    @Transactional
    public void transferItem(UUID itemId, UUID toUserId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        User newOwner = userRepository.findById(toUserId)
            .orElseThrow(() -> new NotFoundException("Target user not found"));

        item.setOwner(newOwner);
        itemRepository.save(item);
    }

    public UUID getCurrentUserId(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new NotFoundException("User not found"));
        return user.getId();
    }

    public boolean isItemOwner(UUID itemId, String userEmail) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found"));
        
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new NotFoundException("User not found"));
        
        return item.getOwner().getId().equals(user.getId());
    }
} 