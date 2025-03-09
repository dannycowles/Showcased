package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeRankingReturnDto {
    private Long showId;
    private Long rankNum;
    private String showTitle;
    private String episodeTitle;
    private int season;
    private int episode;
    private String posterPath;
}
