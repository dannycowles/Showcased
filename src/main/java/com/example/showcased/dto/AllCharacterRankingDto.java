package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCharacterRankingDto {
    private List<CharacterRankingReturnDto> protagonists;
    private List<CharacterRankingReturnDto> deuteragonists;
    private List<CharacterRankingReturnDto> antagonists;
    private List<CharacterRankingReturnDto> tritagonists;
    private List<CharacterRankingReturnDto> side;
}
