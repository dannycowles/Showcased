package com.example.showcased.controller;

import com.example.showcased.dto.WatchlistReturnDto;
import com.example.showcased.dto.WatchlistSendDto;
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
    public ResponseEntity<Void> addShowToWatchlist(@RequestBody WatchlistSendDto show, HttpSession session) {
        profileService.addShowToWatchlist(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<WatchlistReturnDto>> getWatchlist(HttpSession session) {
        List<WatchlistReturnDto> watchlist = profileService.getWatchlist(session);
        return ResponseEntity.ok(watchlist);
    }

}
