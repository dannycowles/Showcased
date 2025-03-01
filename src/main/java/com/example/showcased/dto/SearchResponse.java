package com.example.showcased.dto;

import java.util.List;

public class SearchResponse {
    private List<SearchDto> results;

    public SearchResponse() {}

    public List<SearchDto> getResults() {
        return results;
    }

    public void setResults(List<SearchDto> results) {
        this.results = results;
    }
}
