package com.example.showcased.controller;

import com.example.showcased.dto.EpisodeRankingReturnDto;
import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.dto.UserSearchDto;
import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.service.UserService;
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
}
