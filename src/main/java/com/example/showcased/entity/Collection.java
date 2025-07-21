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

    @Column(name = "is_private")
    private boolean privateCollection = false;

    @Column(name = "is_ranked")
    private boolean ranked = false;

    private int numLikes;

    public Collection(Long userId, String collectionName) {
        this.userId = userId;
        this.collectionName = collectionName;
    }
}
