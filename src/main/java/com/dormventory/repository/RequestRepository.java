package com.dormventory.repository;

import com.dormventory.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findByRoomIdOrderByCreatedAtDesc(UUID roomId);
} 