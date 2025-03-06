package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RankingReturnDto {
    private Long showId;
    private Long rankNum;
    private String title;
    private String posterPath;
}
