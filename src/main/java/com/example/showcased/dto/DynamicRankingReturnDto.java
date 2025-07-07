package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DynamicRankingReturnDto {
    private Long id;
    private String character1Id;
    private String character1Name;
    private String character2Id;
    private String character2Name;
    private String showTitle;
    private int rankNum;
}
