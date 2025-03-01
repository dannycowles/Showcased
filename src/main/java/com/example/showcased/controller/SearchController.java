package com.example.showcased.controller;

import com.example.showcased.dto.SearchDto;
import com.example.showcased.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<SearchDto>> searchShows(@RequestParam String query) {
        List<SearchDto> results = searchService.searchShows(query);
        return ResponseEntity.ok(results);
    }

}
