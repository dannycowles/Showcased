package com.example.showcased.dto;

import lombok.Data;

@Data
public class DynamicRankingDto {
    private String character1Id;
    private String character1Name;
    private String character2Id;
    private String character2Name;
    private Long showId;
    private String showTitle;
    private String posterPath;
}
