package com.dormventory.service;

import com.dormventory.model.Item;
import com.dormventory.model.User;
import com.dormventory.repository.ItemRepository;
import com.dormventory.repository.UserRepository;
import com.dormventory.exception.NotFoundException;
import com.dormventory.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
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
    public void deleteItem(UUID itemId, UUID userId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You are not the owner of this item");
        }

        itemRepository.delete(item);
    }

    @Transactional
    public void transferItem(UUID itemId, UUID fromUserId, UUID toUserId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getOwner().getId().equals(fromUserId)) {
            throw new UnauthorizedException("You are not the owner of this item");
        }

        User newOwner = userRepository.findById(toUserId)
            .orElseThrow(() -> new NotFoundException("Target user not found"));

        item.setOwner(newOwner);
        itemRepository.save(item);
    }
} 