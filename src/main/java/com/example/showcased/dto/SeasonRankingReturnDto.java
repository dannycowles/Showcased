package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonRankingReturnDto {
    private Long id;
    private Long showId;
    private int season;
    private Long rankNum;
    private String posterPath;
    private String showTitle;
}
