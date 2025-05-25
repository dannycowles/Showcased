package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<ProfileDetailsDto> getUserDetails(@PathVariable("id") Long id, HttpSession session) {
        ProfileDetailsDto userDetails = userService.getUserDetails(id, session);
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewWithUserInfoDto>> getUserReviews(@PathVariable Long id) {
        List<ReviewWithUserInfoDto> reviews = userService.getUserReviews(id);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable("id") Long id) {
        Long followersCount = userService.getFollowersCount(id);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserSearchDto>> getFollowing(@PathVariable("id") Long id, HttpSession session) {
        List<UserSearchDto> following = userService.getFollowing(id, session);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable("id") Long id) {
        Long followingCount = userService.getFollowingCount(id);
        return ResponseEntity.ok(followingCount);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserSearchDto>> getFollowers(@PathVariable("id") Long id, HttpSession session) {
        List<UserSearchDto> followers = userService.getFollowers(id, session);
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


    // ========== SOCIAL ==========

    @PostMapping("/{id}/followers")
    public ResponseEntity<Void> followUser(@PathVariable("id") Long followId, HttpSession session) {
        userService.followUser(followId, session);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/followers")
    public ResponseEntity<Void> unfollowUser(@PathVariable("id") Long unfollowId, HttpSession session) {
        userService.unfollowUser(unfollowId, session);
        return ResponseEntity.noContent().build();
    }



    // ========== COLLECTIONS ==========

    @GetMapping("/{id}/collections")
    public ResponseEntity<List<CollectionDto>> getCollections(@PathVariable Long id) {
        List<CollectionDto> collections = userService.getCollections(id);
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{id}/collections/{collectionId}")
    public ResponseEntity<CollectionReturnDto> getCollectionDetails(@PathVariable Long id, @PathVariable Long collectionId) {
        CollectionReturnDto collection = userService.getShowsInCollection(id, collectionId);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/collections/{collectionId}/likes")
    public ResponseEntity<Void> likeCollection(@PathVariable Long collectionId, HttpSession session) {
        userService.likeCollection(collectionId, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/collections/{collectionId}/likes")
    public ResponseEntity<Void> unlikeCollection(@PathVariable Long collectionId, HttpSession session) {
        userService.unlikeCollection(collectionId, session);
        return ResponseEntity.noContent().build();
    }
}
