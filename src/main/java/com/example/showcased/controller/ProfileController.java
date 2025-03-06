package com.example.showcased.controller;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.dto.WatchSendDto;
import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/watchlist")
    public ResponseEntity<Void> addShowToWatchlist(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToWatchlist(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getWatchlist(HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchlist(session);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/watchlist/top")
    public ResponseEntity<List<WatchReturnDto>> getWatchlistTop(HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchlistTop(session);
        return ResponseEntity.ok(watchlist);
    }




    @PostMapping("/watching")
    public ResponseEntity<Void> addShowToWatchingList(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToWatchingList(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watching")
    public ResponseEntity<List<WatchReturnDto>> getWatchingList(HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchingList(session);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/watching/top")
    public ResponseEntity<List<WatchReturnDto>> getWatchingListTop(HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchingListTop(session);
        return ResponseEntity.ok(watchlist);
    }




    @PostMapping("/show-ranking")
    public ResponseEntity<Void> addShowToRankingList(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToRankingList(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/show-ranking")
    public ResponseEntity<List<RankingReturnDto>> getShowRankingList(HttpSession session) {
        List<RankingReturnDto> ranking = profileService.getShowRankingList(session);
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/show-ranking/top")
    public ResponseEntity<List<RankingReturnDto>> getShowRankingListTop(HttpSession session) {
        List<RankingReturnDto> ranking = profileService.getShowRankingListTop(session);
        return ResponseEntity.ok(ranking);
    }
}
