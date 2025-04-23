package com.example.showcased.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateEpisodeRankingDto {
    private Long episodeId;
    private Long rankNum;
}
