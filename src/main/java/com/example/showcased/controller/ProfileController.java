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

    @GetMapping("/details")
    public ResponseEntity<ProfileDetailsDto> getProfileDetails(HttpSession session) {
        ProfileDetailsDto profileDetails = profileService.getProfileDetails(session);
        return ResponseEntity.ok(profileDetails);
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

    @DeleteMapping("/watching/{id}")
    public ResponseEntity<Void> removeFromWatchingList(@PathVariable("id") String id, HttpSession session) {
        profileService.removeFromWatchingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/watching/{id}")
    public ResponseEntity<Void> moveToRankingList(@PathVariable("id") String id, HttpSession session) {
        profileService.moveToRankingList(id, session);
        return ResponseEntity.noContent().build();
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

    @DeleteMapping("/show-ranking/{id}")
    public ResponseEntity<Void> removeFromShowRankingList(@PathVariable("id") String id, HttpSession session) {
        profileService.removeFromShowRankingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/show-ranking")
    public ResponseEntity<Void> updateShowRankingList(@RequestBody List<UpdateShowRankingDto> shows, HttpSession session) {
        profileService.updateShowRankingList(shows, session);
        return ResponseEntity.noContent().build();
    }




    @PostMapping("/episode-ranking")
    public ResponseEntity<Void> addEpisodeToRankingList(@RequestBody EpisodeRankingDto episode, HttpSession session) {
        profileService.addEpisodeToRankingList(episode, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/episode-ranking")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getEpisodeRankingList(@RequestParam(value = "limit", required = false) Integer limit, HttpSession session) {
        List<EpisodeRankingReturnDto> ranking = profileService.getEpisodeRankingList(limit, session);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/episode-ranking/show/{showId}/season/{seasonNumber}/episode/{episodeNumber}")
    public ResponseEntity<Void> removeFromEpisodeRankingList(@PathVariable("showId") Long showId, @PathVariable("seasonNumber") int seasonNumber, @PathVariable("episodeNumber") int episodeNumber, HttpSession session) {
        profileService.removeFromEpisodeRankingList(showId, seasonNumber, episodeNumber, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/episode-ranking")
    public ResponseEntity<Void> updateEpisodeRankingList(@RequestBody List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        profileService.updateEpisodeRankingList(episodes, session);
        return ResponseEntity.noContent().build();
    }




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




    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewWithUserInfoDto>> getReviews(HttpSession session) {
        List<ReviewWithUserInfoDto> reviews = profileService.getReviews(session);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, HttpSession session) {
        String fileUrl = fileService.uploadProfilePicture(file, session);
        return ResponseEntity.ok(fileUrl);
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
}
