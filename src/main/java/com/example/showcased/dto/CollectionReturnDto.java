package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CollectionReturnDto {
    private String collectionName;
    private boolean isPrivate;
    private boolean isRanked;
    private String description;
    private int numLikes;
    private List<RankingReturnDto> shows;
}
