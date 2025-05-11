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

    // ========== SHOW BROWSING ==========

    @GetMapping()
    public ResponseEntity<List<SearchDto>> searchShows(@RequestParam String name) {
        List<SearchDto> results = showService.searchShows(name);
        return ResponseEntity.ok(results);
    }


    // ========== SHOW INFORMATION ==========

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowDetails(@PathVariable String id, HttpSession session) {
        ShowDto show = showService.getShowDetails(id, session);
        return ResponseEntity.ok(show);
    }

    @GetMapping("/{id}/num-seasons")
    public ResponseEntity<NumSeasonsDto> getNumberOfSeasons(@PathVariable int id) {
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


    // ========== REVIEWS ==========

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Void> addReviewToShow(@PathVariable Long id, @RequestBody ReviewDto review, HttpSession session) {
        showService.addReviewToShow(id, review, session);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewWithUserInfoDto> getShowReviews(@PathVariable Long id, HttpSession session) {
        return showService.getShowReviews(id, session);
    }

    @PostMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<Void> likeShowReview(@PathVariable Long reviewId, HttpSession session) {
        showService.likeShowReview(reviewId, session);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<Void> unlikeShowReview(@PathVariable Long reviewId, HttpSession session) {
        showService.unlikeShowReview(reviewId, session);
        return ResponseEntity.ok().build();
    }


    // ========== DISCOVER ==========

    @GetMapping("/trending")
    public ResponseEntity<TrendingShowsDto> getTrendingShows(@RequestParam(required = false) Integer page) {
        TrendingShowsDto shows = showService.getTrendingShows(page);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/genres")
    public ResponseEntity<AllGenresDto> getShowGenres() {
        AllGenresDto genres = showService.getAllGenres();
        return ResponseEntity.ok(genres);
    }
}
