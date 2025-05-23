package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "liked_collections")
public class LikedCollections {
    @EmbeddedId
    private LikedCollectionsId id;
}
