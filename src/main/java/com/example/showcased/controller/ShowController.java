package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.entity.Review;
import com.example.showcased.service.ShowService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/show")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchDto>> searchShows(@RequestParam String query) {
        List<SearchDto> results = showService.searchShows(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowDetails(@PathVariable("id") String id) {
        ShowDto show = showService.getShowDetails(id);
        return ResponseEntity.ok(show);
    }

    @GetMapping("/{id}/season/{seasonNumber}")
    public ResponseEntity<SeasonDto> getSeasonDetails(@PathVariable int seasonNumber, @PathVariable int id) {
        SeasonDto season = showService.getSeasonDetails(seasonNumber, id);
        return ResponseEntity.ok(season);
    }

    @GetMapping("/{id}/season/{seasonNumber}/episode/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeDetails(@PathVariable String episodeNumber, @PathVariable String seasonNumber, @PathVariable String id) {
        EpisodeDto episode = showService.getEpisodeDetails(episodeNumber, seasonNumber, id);
        return ResponseEntity.ok(episode);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Void> addReviewToShow(@PathVariable("id") Long id, @RequestBody ReviewDto review, HttpSession session) {
        showService.addReviewToShow(id, review, session);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewWithUserInfoDto> getShowReviews(@PathVariable Long id) {
        return showService.getShowReviews(id);
    }
}
