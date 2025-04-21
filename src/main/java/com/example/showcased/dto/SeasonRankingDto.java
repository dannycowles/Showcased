package com.example.showcased.dto;

import lombok.Data;

@Data
public class SeasonRankingDto {
    private Long id;
    private Long showId;
    private int season;
    private String posterPath;
    private String showTitle;
}
