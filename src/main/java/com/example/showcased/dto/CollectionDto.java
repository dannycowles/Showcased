package com.example.showcased.dto;

import lombok.Data;

@Data
public class CollectionDto {
    private Long userId;
    private Long collectionId;
    private String collectionName;
    private boolean isPrivate;
}
