package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrendingShowsDto {
    private int page;
    private List<SearchDto> results;
    private int total_pages;
    private int total_results;
}
