package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingReturnDto {
    private Long showId;
    private Long rankNum;
    private String title;
    private String posterPath;
}
