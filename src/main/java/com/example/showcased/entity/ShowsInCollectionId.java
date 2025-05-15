package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ShowsInCollectionId {
    private Long userId;
    private Long collectionId;
}
