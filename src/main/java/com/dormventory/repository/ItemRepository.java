package com.dormventory.repository;

import com.dormventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findByOwnerId(UUID ownerId);
    List<Item> findByRoomId(UUID roomId);
} 