package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ========== USER DETAILS ==========

    @GetMapping()
    public ResponseEntity<List<UserSearchDto>> searchUsers(@RequestParam String query) {
        List<UserSearchDto> searchResults = userService.searchUsers(query);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/{username}/details")
    public ResponseEntity<ProfileDetailsDto> getUserDetails(@PathVariable String username) {
        ProfileDetailsDto userDetails = userService.getUserDetails(username);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/{username}/reviews")
    public ResponseEntity<Page<ShowReviewDto>> getUserReviews(@PathVariable String username,
                                                              @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<ShowReviewDto> reviews = userService.getUserReviews(username, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{username}/show-reviews")
    public ResponseEntity<Page<ShowReviewDto>> getUserShowReviews(@PathVariable String username,
                                                                  @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE)  Pageable pageable) {
        Page<ShowReviewDto> reviews = userService.getUserShowReviews(username, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{username}/episode-reviews")
    public ResponseEntity<Page<EpisodeReviewDto>> getUserEpisodeReviews(@PathVariable String username,
                                                                        @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE)  Pageable pageable) {
        Page<EpisodeReviewDto> reviews = userService.getUserEpisodeReviews(username, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{username}/season-reviews")
    public ResponseEntity<Page<SeasonReviewDto>> getUserSeasonReviews(@PathVariable String username,
                                                                      @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE)  Pageable pageable) {
        Page<SeasonReviewDto> reviews = userService.getUserSeasonReviews(username, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<Page<UserSearchDto>> getFollowing(@PathVariable String username,
                                                            @RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> following = userService.getFollowing(username, name, pageable);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<Page<UserSearchDto>> getFollowers(@PathVariable String username,
                                                            @RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> followers = userService.getFollowers(username, name, pageable);
        return ResponseEntity.ok(followers);
    }


    // ========== USER LISTS ==========

    @GetMapping("/{username}/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchlist(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchlist = userService.getUserWatchlist(username, limit);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/{username}/currently-watching")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchingList(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchingList = userService.getUserWatchingList(username, limit);
        return ResponseEntity.ok(watchingList);
    }

    @GetMapping("/{username}/show-rankings")
    public ResponseEntity<List<RankingReturnDto>> getUserShowRankings(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit) {
        List<RankingReturnDto> rankings =  userService.getUserShowRankings(username, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{username}/episode-rankings")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getUserEpisodeRankings(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit) {
        List<EpisodeRankingReturnDto> rankings = userService.getUserEpisodeRankings(username, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{username}/season-rankings")
    public ResponseEntity<List<SeasonRankingReturnDto>> getUserSeasonRankings(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit) {
        List<SeasonRankingReturnDto> rankings = userService.getUserSeasonRankings(username, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{username}/character-rankings")
    public ResponseEntity<?> getUserCharacterRankings(@PathVariable String username, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "type", required = false) String characterType) {
        if (characterType == null) {
            return ResponseEntity.ok(userService.getAllUserCharacterRankings(username, limit));
        } else {
            return ResponseEntity.ok(userService.getUserCharacterRankings(username, limit, characterType));
        }
    }

    @GetMapping("/{username}/character-dynamics")
    public ResponseEntity<List<DynamicRankingReturnDto>> getUserDynamicRankings(@PathVariable String username, @RequestParam(required = false) Integer limit) {
        List<DynamicRankingReturnDto> rankings = userService.getUserDynamicRankings(username, limit);
        return ResponseEntity.ok(rankings);
    }


    // ========== SOCIAL ==========

    @PostMapping("/{id}/followers")
    public ResponseEntity<Void> followUser(@PathVariable("id") Long followId) {
        userService.followUser(followId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/followers")
    public ResponseEntity<Void> unfollowUser(@PathVariable("id") Long unfollowId) {
        userService.unfollowUser(unfollowId);
        return ResponseEntity.noContent().build();
    }



    // ========== COLLECTIONS ==========

    @GetMapping("/{username}/collections")
    public ResponseEntity<Page<CollectionDto>> getCollections(@RequestParam(required = false) String name, @PathVariable String username,
                                                              @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<CollectionDto> collections = userService.getCollections(name, username, pageable);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<CollectionReturnDto> getCollectionDetails(@PathVariable Long collectionId) {
        CollectionReturnDto collection = userService.getShowsInCollection(collectionId);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/collections/{collectionId}/likes")
    public ResponseEntity<Void> likeCollection(@PathVariable Long collectionId) {
        userService.likeCollection(collectionId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/collections/{collectionId}/likes")
    public ResponseEntity<Void> unlikeCollection(@PathVariable Long collectionId) {
        userService.unlikeCollection(collectionId);
        return ResponseEntity.noContent().build();
    }
}
