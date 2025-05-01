package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.FileService;
import com.example.showcased.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final FileService fileService;

    public ProfileController(ProfileService profileService,
                             FileService fileService) {
        this.profileService = profileService;
        this.fileService = fileService;
    }

    // ========== PROFILE DETAILS ==========

    @GetMapping("/details")
    public ResponseEntity<ProfileDetailsDto> getProfileDetails(HttpSession session) {
        ProfileDetailsDto profileDetails = profileService.getProfileDetails(session);
        return ResponseEntity.ok(profileDetails);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, HttpSession session) {
        String fileUrl = fileService.uploadProfilePicture(file, session);
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewWithUserInfoDto>> getReviews(HttpSession session) {
        List<ReviewWithUserInfoDto> reviews = profileService.getReviews(session);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserSearchDto>> getFollowers(HttpSession session) {
        List<UserSearchDto> followers = profileService.getFollowers(session);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/followers/count")
    public ResponseEntity<Long> getFollowersCount(HttpSession session) {
        Long followersCount = profileService.getFollowersCount(session);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserSearchDto>> getFollowing(HttpSession session) {
        List<UserSearchDto> following = profileService.getFollowing(session);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/following/count")
    public ResponseEntity<Long> getFollowingCount(HttpSession session) {
        Long followingCount = profileService.getFollowingCount(session);
        return ResponseEntity.ok(followingCount);
    }

    @DeleteMapping("/followers/{id}")
    public ResponseEntity<Void> removeFollower(@PathVariable("id") Long id, HttpSession session) {
        profileService.removeFollower(id, session);
        return ResponseEntity.noContent().build();
    }


    // ========= WATCHLIST ==========

    @PostMapping("/watchlist")
    public ResponseEntity<Void> addShowToWatchlist(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToWatchlist(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getWatchlist(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchlist(limit, session);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/watchlist/{id}")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable("id") String id, HttpSession session) {
        profileService.removeFromWatchlist(id, session);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/watchlist/{id}")
    public ResponseEntity<Void> moveToWatchingList(@PathVariable("id") String id, HttpSession session) {
        profileService.moveToWatchingList(id, session);
        return ResponseEntity.noContent().build();
    }


    // ========== CURRENTLY WATCHING ==========

    @PostMapping("/currently-watching")
    public ResponseEntity<Void> addShowToWatchingList(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToWatchingList(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/currently-watching")
    public ResponseEntity<List<WatchReturnDto>> getWatchingList(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<WatchReturnDto> watchlist = profileService.getWatchingList(limit, session);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/currently-watching/{id}")
    public ResponseEntity<Void> removeFromWatchingList(@PathVariable("id") String id, HttpSession session) {
        profileService.removeFromWatchingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/currently-watching/{id}")
    public ResponseEntity<Void> moveToRankingList(@PathVariable("id") String id, HttpSession session) {
        profileService.moveToRankingList(id, session);
        return ResponseEntity.noContent().build();
    }


    // ========== SHOW RANKINGS ==========

    @PostMapping("/show-ranking")
    public ResponseEntity<Void> addShowToRankingList(@RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToRankingList(show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/show-rankings")
    public ResponseEntity<List<RankingReturnDto>> getShowRankingList(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<RankingReturnDto> ranking = profileService.getShowRankingList(limit, session);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/show-rankings/{id}")
    public ResponseEntity<Void> removeFromShowRankingList(@PathVariable("id") String id, HttpSession session) {
        profileService.removeFromShowRankingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/show-rankings")
    public ResponseEntity<Void> updateShowRankingList(@RequestBody List<UpdateShowRankingDto> shows, HttpSession session) {
        profileService.updateShowRankingList(shows, session);
        return ResponseEntity.noContent().build();
    }


    // ========== EPISODE RANKINGS ==========

    @PostMapping("/episode-rankings")
    public ResponseEntity<Void> addEpisodeToRankingList(@RequestBody EpisodeRankingDto episode, HttpSession session) {
        profileService.addEpisodeToRankingList(episode, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/episode-rankings")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getEpisodeRankingList(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<EpisodeRankingReturnDto> ranking = profileService.getEpisodeRankingList(limit, session);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/episode-rankings/{id}")
    public ResponseEntity<Void> removeFromEpisodeRankingList(@PathVariable("id") Long id, HttpSession session) {
        profileService.removeFromEpisodeRankingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/episode-rankings")
    public ResponseEntity<Void> updateEpisodeRankingList(@RequestBody List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        profileService.updateEpisodeRankingList(episodes, session);
        return ResponseEntity.noContent().build();
    }


    // ========== SEASON RANKINGS ==========

    @PostMapping("/season-rankings")
    public ResponseEntity<Void> addSeasonToRankingList(@RequestBody SeasonRankingDto season, HttpSession session) {
        profileService.addSeasonToRankingList(season, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/season-rankings")
    public ResponseEntity<List<SeasonRankingReturnDto>> getSeasonRankingList(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<SeasonRankingReturnDto> ranking = profileService.getSeasonRankingList(limit, session);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/season-rankings/{id}")
    public ResponseEntity<Void> removeFromSeasonRankingList(@PathVariable("id") Long seasonId, HttpSession session) {
        profileService.removeFromSeasonRankingList(seasonId, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/season-rankings")
    public ResponseEntity<Void> updateSeasonRankingList(@RequestBody List<UpdateSeasonRankingDto> seasons, HttpSession session) {
        profileService.updateSeasonRankingList(seasons, session);
        return ResponseEntity.noContent().build();
    }


    // ========== CHARACTER RANKINGS ==========

    @PostMapping("/character-rankings")
    public ResponseEntity<Void> addCharacterToRankingList(@RequestBody CharacterRankingDto character, HttpSession session) {
        profileService.addCharacterToRankingList(character, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/character-rankings")
    public ResponseEntity<List<CharacterRankingReturnDto>> getCharacterRankingList(@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "type") String characterType, HttpSession session) {
        List<CharacterRankingReturnDto> ranking = profileService.getCharacterRankingList(limit, characterType, session);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/character-rankings/type/{characterType}/{name}")
    public ResponseEntity<Void> removeFromCharacterRankingList(@PathVariable("characterType") String characterType, @PathVariable("name") String name, HttpSession session) {
        profileService.removeFromCharacterRankingList(characterType, name, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/character-rankings")
    public ResponseEntity<Void> updateCharacterRankingList(@RequestBody List<UpdateCharacterRankingDto> characters, HttpSession session) {

        return null;
    }

}
