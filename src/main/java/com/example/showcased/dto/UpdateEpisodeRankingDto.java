package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEpisodeRankingDto {
    private Long showId;
    private Long rankNum;
    private int season;
    private int episode;
}
