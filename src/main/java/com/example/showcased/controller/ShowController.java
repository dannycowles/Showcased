package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.ShowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final ShowService showService;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    // ========== SHOW BROWSING ==========

    @GetMapping()
    public ResponseEntity<ShowResultsPageDto> searchShows(@RequestParam(required = false) String name, @RequestParam(required = false) Integer genre, @RequestParam(required = false) Integer page) {
        if (genre != null) {
            return ResponseEntity.ok(showService.searchByGenre(genre, page));
        } else if (name != null) {
            return ResponseEntity.ok(showService.searchByTitle(name, page));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    // ========== SHOW INFORMATION ==========

    @GetMapping("/{id}")
    public ResponseEntity<ShowDto> getShowDetails(@PathVariable String id) {
        ShowDto show = showService.getShowDetails(id);
        return ResponseEntity.ok(show);
    }

    @GetMapping("/{id}/num-seasons")
    public ResponseEntity<NumSeasonsDto> getNumberOfSeasons(@PathVariable String id) {
        NumSeasonsDto numSeasons = showService.getNumberOfSeasons(id);
        return ResponseEntity.ok(numSeasons);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}")
    public ResponseEntity<SeasonDto> getSeasonDetails(@PathVariable String seasonNumber, @PathVariable String id) {
        SeasonDto season = showService.getSeasonDetails(seasonNumber, id);
        return ResponseEntity.ok(season);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}/episodes")
    public ResponseEntity<ShowEpisodesDto> getSeasonEpisodes(@PathVariable String id, @PathVariable String seasonNumber) {
        ShowEpisodesDto episodes = showService.getSeasonEpisodes(id, seasonNumber);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/{id}/seasons/{seasonNumber}/episodes/{episodeNumber}")
    public ResponseEntity<EpisodeDto> getEpisodeDetails(@PathVariable String episodeNumber, @PathVariable String seasonNumber, @PathVariable String id) {
        EpisodeDto episode = showService.getEpisodeDetails(episodeNumber, seasonNumber, id);
        return ResponseEntity.ok(episode);
    }

    @GetMapping("/{id}/characters")
    public ResponseEntity<List<RoleDto>> getShowCharacters(@PathVariable String id, @RequestParam(required = false) String name) {
        List<RoleDto> characters = showService.getCharacters(id, name);
        return ResponseEntity.ok(characters);
    }


    // ========== SHOW REVIEWS ==========

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ShowReviewWithUserInfoDto> addReviewToShow(@PathVariable Long id, @RequestBody ShowReviewDto review) {
        ShowReviewWithUserInfoDto newReview = showService.addReviewToShow(id, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<ShowReviewWithUserInfoDto>> getShowReviews(@PathVariable Long id,
                                                                          @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE, sort = "reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ShowReviewWithUserInfoDto> reviews = showService.getShowReviews(id, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ShowReviewWithUserInfoDto> getShowReview(@PathVariable Long reviewId) {
        ShowReviewWithUserInfoDto review = showService.getShowReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<Void> likeShowReview(@PathVariable Long reviewId) {
        showService.likeShowReview(reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/reviews/{reviewId}/likes")
    public ResponseEntity<Void> unlikeShowReview(@PathVariable Long reviewId) {
        showService.unlikeShowReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteShowReview(@PathVariable Long reviewId) {
        showService.deleteShowReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> updateShowReview(@PathVariable Long reviewId, @RequestBody UpdateReviewDto updates) {
        showService.updateShowReview(reviewId, updates);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<ReviewCommentWithUserInfoDto> addCommentToShowReview(@PathVariable Long reviewId, @RequestBody ReviewCommentDto reviewComment) {
        ReviewCommentWithUserInfoDto newComment = showService.addCommentToShowReview(reviewId, reviewComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping("/reviews/{reviewId}/comments")
    public ResponseEntity<Page<ReviewCommentWithUserInfoDto>> getShowReviewComments(@PathVariable Long reviewId, @RequestParam(required = false, defaultValue = "1") int page) {
        Page<ReviewCommentWithUserInfoDto> comments = showService.getShowReviewComments(reviewId, page);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/reviews/comments/{commentId}")
    public ResponseEntity<ReviewCommentWithUserInfoDto> getShowReviewComment(@PathVariable Long commentId) {
        ReviewCommentWithUserInfoDto comment = showService.getShowReviewComment(commentId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/reviews/comments/{commentId}/likes")
    public ResponseEntity<Void> likeShowReviewComment(@PathVariable Long commentId) {
        showService.likeShowReviewComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/reviews/comments/{commentId}/likes")
    public ResponseEntity<Void> unlikeShowReviewComment(@PathVariable Long commentId) {
        showService.unlikeShowReviewComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reviews/comments/{commentId}")
    public ResponseEntity<Void> deleteShowReviewComment(@PathVariable Long commentId) {
        showService.deleteShowReviewComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reviews/comments/{commentId}")
    public ResponseEntity<Void> updateShowReviewComment(@PathVariable Long commentId, @RequestBody UpdateCommentDto updates) {
        showService.updateShowReviewComment(commentId, updates);
        return ResponseEntity.noContent().build();
    }



    // ========== SEASON REVIEWS ==========

    @PostMapping("/seasons/{seasonId}/reviews")
    public ResponseEntity<SeasonReviewWithUserInfoDto> addReviewToSeason(@PathVariable Long seasonId, @RequestBody SeasonReviewDto review) {
        SeasonReviewWithUserInfoDto newReview = showService.addReviewToSeason(seasonId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @GetMapping("/seasons/{seasonId}/reviews")
    public ResponseEntity<Page<SeasonReviewWithUserInfoDto>> getSeasonReviews(@PathVariable Long seasonId,
                                                                              @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE, sort = "reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SeasonReviewWithUserInfoDto> reviews = showService.getSeasonReviews(seasonId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/season-reviews/{reviewId}")
    public ResponseEntity<SeasonReviewWithUserInfoDto> getSeasonReview(@PathVariable Long reviewId) {
        SeasonReviewWithUserInfoDto review = showService.getSeasonReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/season-reviews/{reviewId}/likes")
    public ResponseEntity<Void> likeSeasonReview(@PathVariable Long reviewId) {
        showService.likeSeasonReview(reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/season-reviews/{reviewId}/likes")
    public ResponseEntity<Void> unlikeSeasonReview(@PathVariable Long reviewId) {
        showService.unlikeSeasonReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/season-reviews/{reviewId}")
    public ResponseEntity<Void> deleteSeasonReview(@PathVariable Long reviewId) {
        return null;
    }



    // ========== EPISODE REVIEWS ==========

    @PostMapping("/episodes/{episodeId}/reviews")
    public ResponseEntity<EpisodeReviewWithUserInfoDto> addReviewToEpisode(@PathVariable Long episodeId, @RequestBody EpisodeReviewDto review) {
        EpisodeReviewWithUserInfoDto newReview = showService.addReviewToEpisode(episodeId, review);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @GetMapping("/episodes/{episodeId}/reviews")
    public ResponseEntity<Page<EpisodeReviewWithUserInfoDto>> getEpisodeReviews(@PathVariable Long episodeId,
                                                                                @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE, sort = "reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EpisodeReviewWithUserInfoDto> reviews = showService.getEpisodeReviews(episodeId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/episode-reviews/{reviewId}")
    public ResponseEntity<EpisodeReviewWithUserInfoDto> getEpisodeReview(@PathVariable Long reviewId) {
        EpisodeReviewWithUserInfoDto review = showService.getEpisodeReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/episode-reviews/{reviewId}/likes")
    public ResponseEntity<Void> likeEpisodeReview(@PathVariable Long reviewId) {
        showService.likeEpisodeReview(reviewId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/episode-reviews/{reviewId}/likes")
    public ResponseEntity<Void> unlikeEpisodeReview(@PathVariable Long reviewId) {
        showService.unlikeEpisodeReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/episode-reviews/{reviewId}")
    public ResponseEntity<Void> deleteEpisodeReview(@PathVariable Long reviewId) {
        showService.deleteEpisodeReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/episode-reviews/{reviewId}")
    public ResponseEntity<Void> updateEpisodeReview(@PathVariable Long reviewId, @RequestBody UpdateReviewDto updates) {
        showService.updateEpisodeReview(reviewId, updates);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/episode-reviews/{reviewId}/comments")
    public ResponseEntity<ReviewCommentWithUserInfoDto> addCommentToEpisodeReview(@PathVariable Long reviewId, @RequestBody ReviewCommentDto reviewComment) {
        ReviewCommentWithUserInfoDto newComment = showService.addCommentToEpisodeReview(reviewId, reviewComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping("/episode-reviews/{reviewId}/comments")
    public ResponseEntity<Page<ReviewCommentWithUserInfoDto>> getEpisodeReviewComments(@PathVariable Long reviewId, @RequestParam(required = false, defaultValue = "1") int page) {
        Page<ReviewCommentWithUserInfoDto> comments = showService.getEpisodeReviewComments(reviewId, page);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/episode-reviews/comments/{commentId}")
    public ResponseEntity<ReviewCommentWithUserInfoDto> getEpisodeReviewComment(@PathVariable Long commentId) {
        ReviewCommentWithUserInfoDto comment = showService.getEpisodeReviewComment(commentId);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/episode-reviews/comments/{commentId}/likes")
    public ResponseEntity<Void> likeEpisodeReviewComment(@PathVariable Long commentId) {
        showService.likeEpisodeReviewComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/episode-reviews/comments/{commentId}/likes")
    public ResponseEntity<Void> unlikeEpisodeReviewComment(@PathVariable Long commentId) {
        showService.unlikeEpisodeReviewComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/episode-reviews/comments/{commentId}")
    public ResponseEntity<Void> deleteEpisodeReviewComment(@PathVariable Long commentId) {
        showService.deleteEpisodeReviewComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/episode-reviews/comments/{commentId}")
    public ResponseEntity<Void> updateEpisodeReviewComment(@PathVariable Long commentId, @RequestBody UpdateCommentDto updates) {
        showService.updateEpisodeReviewComment(commentId, updates);
        return ResponseEntity.noContent().build();
    }




    // ========== DISCOVER ==========

    @GetMapping("/trending")
    public ResponseEntity<ShowResultsPageDto> getTrendingShows(@RequestParam(required = false) Integer page) {
        ShowResultsPageDto shows = showService.getTrendingShows(page);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/genres")
    public ResponseEntity<AllGenresDto> getShowGenres() {
        AllGenresDto genres = showService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/top")
    public ResponseEntity<ShowResultsPageDto> getTopRatedShows(@RequestParam(required = false) Integer page) {
        ShowResultsPageDto shows = showService.getTopRatedShows(page);
        return ResponseEntity.ok(shows);
    }
}
