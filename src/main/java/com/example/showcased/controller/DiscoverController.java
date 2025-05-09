package com.example.showcased.controller;

import com.example.showcased.dto.TrendingShowsDto;
import com.example.showcased.service.DiscoverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discover")
public class DiscoverController {

    private final DiscoverService discoverService;

    public DiscoverController(DiscoverService discoverService) {
        this.discoverService = discoverService;
    }

    @GetMapping("/shows/trending")
    public ResponseEntity<TrendingShowsDto> getTrendingShows(@RequestParam(value = "page", required = false) Integer page) {
        TrendingShowsDto shows = discoverService.getTrendingShows(page);
        return ResponseEntity.ok(shows);
    }
}
