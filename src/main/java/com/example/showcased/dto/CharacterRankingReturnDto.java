package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterRankingReturnDto {
    private String id;
    private Long showId;
    private String name;
    private String showName;
    private int rankNum;
}
