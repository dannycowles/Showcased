package com.example.showcased.dto;

import lombok.Data;

@Data
public class UpdateCharacterRankingDto {
    private String characterName;
    private String showName;
    private int rankNum;
}
