package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCharacterRankingsDto {
    private String characterType;
    private List<UpdateCharacterRankingDto> updates;
}
