package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionReturnDto {
    private String name;
    private boolean isPrivate;
    private boolean isRanked;
    private String description;
    private int numLikes;
    private List<RankingReturnDto> shows;
    private boolean isLikedByUser;
}
