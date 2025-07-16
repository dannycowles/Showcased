package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EpisodeRankingReturnDto {
    private Long episodeId;
    private Long showId;
    private Long rankNum;
    private String showTitle;
    private String episodeTitle;
    private int season;
    private int episode;
    private String posterPath;
}
