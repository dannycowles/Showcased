package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class ShowResultsPageDto {
    private int page;
    private List<SearchDto> results;

    @JsonAlias("total_pages")
    private int totalPages;

    @JsonAlias("total_results")
    private int totalResults;
}
