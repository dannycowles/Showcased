package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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


    @GetMapping("/{id}/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchlist(@PathVariable Long id) {
        List<WatchReturnDto> watchlist = userService.getUserWatchlist(id);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/{id}/watchlist/top")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchlistTop(@PathVariable Long id) {
        List<WatchReturnDto> watchlist = userService.getUserWatchlistTop(id);
        return ResponseEntity.ok(watchlist);
    }




    @GetMapping("/{id}/watching")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchingList(@PathVariable Long id) {
        List<WatchReturnDto> watchingList = userService.getUserWatchingList(id);
        return ResponseEntity.ok(watchingList);
    }

    @GetMapping("/{id}/watching/top")
    public ResponseEntity<List<WatchReturnDto>> getUserWatchingListTop(@PathVariable Long id) {
        List<WatchReturnDto> watchingList = userService.getUserWatchingListTop(id);
        return ResponseEntity.ok(watchingList);
    }




    @GetMapping("/{id}/show-ranking")
    public ResponseEntity<List<RankingReturnDto>> getUserShowRankings(@PathVariable Long id) {
        List<RankingReturnDto> rankings =  userService.getUserShowRankings(id);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("/{id}/show-ranking/top")
    public ResponseEntity<List<RankingReturnDto>> getUserShowRankingsTop(@PathVariable Long id) {
        List<RankingReturnDto> rankings =  userService.getUserShowRankingsTop(id);
        return ResponseEntity.ok(rankings);
    }




    @GetMapping("/{id}/episode-ranking")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getUserEpisodeRankings(@PathVariable Long id) {
        List<EpisodeRankingReturnDto> rankings = userService.getUserEpisodeRankings(id);
        return ResponseEntity.ok(rankings);
    }

    @GetMapping("{id}/episode-ranking/top")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getUserEpisodeRankingsTop(@PathVariable Long id) {
        List<EpisodeRankingReturnDto> rankings = userService.getUserEpisodeRankingsTop(id);
        return ResponseEntity.ok(rankings);
    }



    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewWithUserInfoDto>> getUserReviews(@PathVariable Long id) {
        List<ReviewWithUserInfoDto> reviews = userService.getUserReviews(id);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("{id}/follow")
    public ResponseEntity<Void> followUser(@PathVariable("id") Long followId, HttpSession session) {
        userService.followUser(followId, session);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable("id") Long unfollowId, HttpSession session) {
        userService.unfollowUser(unfollowId, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserSearchDto>> getFollowers(@PathVariable("id") Long id) {
        List<UserSearchDto> followers = userService.getFollowers(id);
        return ResponseEntity.ok(followers);
    }

    @DeleteMapping("/followers/{id}")
    public ResponseEntity<Void> removeFollower(@PathVariable("id") Long id, HttpSession session) {
        userService.removeFollower(id, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable("id") Long id) {
        Long followersCount = userService.getFollowersCount(id);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserSearchDto>> getFollowing(@PathVariable("id") Long id) {
        List<UserSearchDto> following = userService.getFollowing(id);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable("id") Long id) {
        Long followingCount = userService.getFollowingCount(id);
        return ResponseEntity.ok(followingCount);
    }
}
