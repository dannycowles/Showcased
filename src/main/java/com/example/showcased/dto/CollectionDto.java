package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CollectionDto {
    private Long userId;
    @JsonProperty("id")
    private Long collectionId;
    private String name;
    private boolean isPrivate;
}
