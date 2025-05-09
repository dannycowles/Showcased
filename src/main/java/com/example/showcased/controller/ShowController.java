package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.ShowService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping()
    public ResponseEntity<List<SearchDto>> searchShows(@RequestParam(value = "name", required = true) String name) {
        List<SearchDto> results = showService.searchShows(name);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowDetails(@PathVariable("id") String id, HttpSession session) {
        ShowDto show = showService.getShowDetails(id, session);
        return ResponseEntity.ok(show);
    }

    @GetMapping("/{id}/num-seasons")
    public ResponseEntity<NumSeasonsDto> getNumberOfSeasons(@PathVariable("id") int id) {
        NumSeasonsDto numSeasons = showService.getNumberOfSeasons(id);
        return ResponseEntity.ok(numSeasons);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}")
    public ResponseEntity<SeasonDto> getSeasonDetails(@PathVariable int seasonNumber, @PathVariable int id, HttpSession session) {
        SeasonDto season = showService.getSeasonDetails(seasonNumber, id, session);
        return ResponseEntity.ok(season);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}/episodes/{episodeNumber}")
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
    public List<ReviewWithUserInfoDto> getShowReviews(@PathVariable Long id, HttpSession session) {
        return showService.getShowReviews(id, session);
    }

    @PatchMapping("/{reviewId}/like")
    public ResponseEntity<Void> likeShowReview(@PathVariable Long reviewId, HttpSession session) {
        showService.likeShowReview(reviewId, session);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reviewId}/unlike")
    public ResponseEntity<Void> unlikeShowReview(@PathVariable Long reviewId, HttpSession session) {
        showService.unlikeShowReview(reviewId, session);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trending")
    public ResponseEntity<TrendingShowsDto> getTrendingShows(@RequestParam(value = "page", required = false) Integer page) {
        TrendingShowsDto shows = showService.getTrendingShows(page);
        return ResponseEntity.ok(shows);
    }

}
