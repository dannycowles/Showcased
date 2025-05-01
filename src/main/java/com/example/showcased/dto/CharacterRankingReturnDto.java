package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CharacterRankingReturnDto {
    private String characterName;
    private String showName;
    private int rankNum;
}
