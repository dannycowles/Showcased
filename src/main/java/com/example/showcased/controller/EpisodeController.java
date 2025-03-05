package com.example.showcased.controller;

import com.example.showcased.dto.EpisodeDto;
import com.example.showcased.service.EpisodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/{episodeNumber}/season/{seasonNumber}/show/{showId}")
    public ResponseEntity<EpisodeDto> getEpisodeDetails(@PathVariable String episodeNumber, @PathVariable String seasonNumber, @PathVariable String showId) {
        EpisodeDto episode = episodeService.getEpisodeDetails(episodeNumber, seasonNumber, showId);
        return ResponseEntity.ok(episode);
    }
}
