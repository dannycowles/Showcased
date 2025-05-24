package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_collections")
@NoArgsConstructor
public class Collection {
    private Long userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collectionId;
    private String collectionName;
    private String description;
    private boolean isPrivate = false;
    private boolean isRanked = false;

    public Collection(Long userId, String collectionName) {
        this.userId = userId;
        this.collectionName = collectionName;
    }
}
