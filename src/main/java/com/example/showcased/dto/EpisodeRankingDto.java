package com.example.showcased.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class EpisodeRankingDto {
    private Long episodeId;
    private Long showId;
    private String showTitle;
    private String episodeTitle;
    private int season;
    private int episode;
    private String posterPath;
}
