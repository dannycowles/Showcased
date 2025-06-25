package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionReturnDto {
    private String name;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    @JsonProperty("isRanked")
    private boolean isRanked;
    private String description;
    private int numLikes;
    private List<RankingReturnDto> shows;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;
}
