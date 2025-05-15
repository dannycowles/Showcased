package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_collections")
public class UserCollection {
    private Long userId;
    @Id
    private Long collectionId;
    private String collectionName;
    private boolean isPrivate;
}
