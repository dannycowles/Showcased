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

    private static final int DEFAULT_PAGE_SIZE = 10;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ========== USER DETAILS ==========

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchDto>> searchUsers(@RequestParam String query) {
        List<UserSearchDto> searchResults = userService.searchUsers(query);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ProfileDetailsDto> getUserDetails(@PathVariable("id") Long id) {
        ProfileDetailsDto userDetails = userService.getUserDetails(id);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<ShowReviewDto>> getUserReviews(@PathVariable Long id,
                                                              @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<ShowReviewDto> reviews = userService.getUserReviews(id, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/show-reviews")
    public ResponseEntity<Page<ShowReviewDto>> getUserShowReviews(@PathVariable Long id,
                                                                  @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE)  Pageable pageable) {
        Page<ShowReviewDto> reviews = userService.getUserShowReviews(id, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/episode-reviews")
    public ResponseEntity<Page<EpisodeReviewDto>> getUserEpisodeReviews(@PathVariable Long id,
                                                                        @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE)  Pageable pageable) {
        Page<EpisodeReviewDto> reviews = userService.getUserEpisodeReviews(id, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<Page<UserSearchDto>> getFollowing(@PathVariable("id") Long id,
                                                            @RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> following = userService.getFollowing(id, name, pageable);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<Page<UserSearchDto>> getFollowers(@PathVariable("id") Long id,
                                                            @RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> followers = userService.getFollowers(id, name, pageable);
        return ResponseEntity.ok(followers);
    }


    // ========== USER LISTS ==========

    @GetMapping("/{id}/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchlist(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchlist = userService.getUserWatchlist(id, limit);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/{id}/currently-watching")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchingList(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchingList = userService.getUserWatchingList(id, limit);
        return ResponseEntity.ok(watchingList);
    }

    @GetMapping("/{id}/show-rankings")
    public ResponseEntity<List<RankingReturnDto>> getUserShowRankings(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        List<RankingReturnDto> rankings =  userService.getUserShowRankings(id, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}/episode-rankings")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getUserEpisodeRankings(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        List<EpisodeRankingReturnDto> rankings = userService.getUserEpisodeRankings(id, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}/season-rankings")
    public ResponseEntity<List<SeasonRankingReturnDto>> getUserSeasonRankings(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        List<SeasonRankingReturnDto> rankings = userService.getUserSeasonRankings(id, limit);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}/character-rankings")
    public ResponseEntity<?> getUserCharacterRankings(@PathVariable Long id, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "type", required = false) String characterType) {
        if (characterType == null) {
            return ResponseEntity.ok(userService.getAllUserCharacterRankings(id, limit));
        } else {
            return ResponseEntity.ok(userService.getUserCharacterRankings(id, limit, characterType));
        }
    }

    @GetMapping("/{id}/character-dynamics")
    public ResponseEntity<List<DynamicRankingReturnDto>> getUserDynamicRankings(@PathVariable Long id, @RequestParam(required = false) Integer limit) {
        List<DynamicRankingReturnDto> rankings = userService.getUserDynamicRankings(id, limit);
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

    @GetMapping("/{id}/collections")
    public ResponseEntity<List<CollectionDto>> getCollections(@RequestParam(required = false) String name, @PathVariable Long id) {
        List<CollectionDto> collections = userService.getCollections(name, id);
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
