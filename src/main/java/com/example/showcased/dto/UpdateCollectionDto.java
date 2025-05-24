package com.example.showcased.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateCollectionDto {
    private String collectionName;
    private String description;
    private Boolean isPrivate;
    private Boolean isRanked;
    private List<UpdateCollectionRankingDto> shows;
}
