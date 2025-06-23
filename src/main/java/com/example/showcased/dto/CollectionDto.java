package com.example.showcased.dto;

import lombok.Data;

@Data
public class CollectionDto {
    private Long userId;
    private Long id;
    private String name;
    private boolean isPrivate;
}
