package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodeRankingDto {
    private Long showId;
    private String title;
    private int season;
    private int episode;
    private String posterPath;
}
