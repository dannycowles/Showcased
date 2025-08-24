package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CollectionDto {
    private Long userId;
    @JsonProperty("id")
    private Long collectionId;
    private String name;
    private boolean isPrivate;
    private List<String> showPosters;
    private int showCount;

    // Constructor for native query results (comma-separated posters)
    public CollectionDto(Long userId, Long collectionId, String name, Boolean isPrivate, String posters, int showCount) {
        this.userId = userId;
        this.collectionId = collectionId;
        this.name = name;
        this.isPrivate = isPrivate != null ? isPrivate : false;
        this.showPosters = posters != null ?
                Arrays.stream(posters.split(","))
                        .collect(Collectors.toList()) :
                new ArrayList<>();
        this.showCount = showCount;
    }
}
