package com.example.showcased.controller;

import com.example.showcased.dto.SeasonDto;
import com.example.showcased.service.SeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/season")
public class SeasonController {

    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping("/{seasonNumber}/show/{showId}")
    public ResponseEntity<SeasonDto> getSeasonByNumberAndShowId(@PathVariable int seasonNumber, @PathVariable int showId) {
        SeasonDto season = seasonService.getSeasonByNumberAndShowId(seasonNumber, showId);
        return ResponseEntity.ok(season);
    }
}
