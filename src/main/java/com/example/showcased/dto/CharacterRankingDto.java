package com.example.showcased.dto;

import lombok.Data;

@Data
public class CharacterRankingDto {
    private String characterId;
    private Long showId;
    private String characterName;
    private String characterType;
    private String showTitle;
    private String posterPath;
}
