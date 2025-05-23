package com.example.showcased.dto;

import lombok.Data;

@Data
public class UpdateCollectionDto {
    private String collectionName;
    private String description;
    private Boolean isPrivate;
}
