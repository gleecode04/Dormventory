package com.dormventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "room")
    private List<User> users;
} 