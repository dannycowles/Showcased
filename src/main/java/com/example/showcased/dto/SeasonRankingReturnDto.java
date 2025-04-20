package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeasonRankingReturnDto {
    private Long showId;
    private int season;
    private Long rankNum;
    private String posterPath;
}
