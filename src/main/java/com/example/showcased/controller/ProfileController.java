package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.FileService;
import com.example.showcased.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
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

    @PatchMapping("/details")
    public ResponseEntity<Void> updateProfileDetails(@RequestBody UpdateBioDto update, HttpSession session) {
        profileService.updateProfileDetails(update, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activity")
    public ResponseEntity<List<ActivityDto>> getProfileActivity(HttpSession session) {
        List<ActivityDto> activity = profileService.getProfileActivity(session);
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, HttpSession session) {
        String fileUrl = fileService.uploadProfilePicture(file, session);
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ShowReviewWithUserInfoDto>> getShowReviews(HttpSession session) {
        List<ShowReviewWithUserInfoDto> reviews = profileService.getShowReviews(session);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/episode-reviews")
    public ResponseEntity<List<EpisodeReviewWithUserInfoDto>> getEpisodeReviews(HttpSession session) {
        List<EpisodeReviewWithUserInfoDto> reviews = profileService.getEpisodeReviews(session);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserSearchDto>> getFollowers(@RequestParam(required = false) String name, HttpSession session) {
        List<UserSearchDto> followers = profileService.getFollowers(name, session);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/followers/count")
    public ResponseEntity<Long> getFollowersCount(HttpSession session) {
        Long followersCount = profileService.getFollowersCount(session);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/following")
    public ResponseEntity<List<UserSearchDto>> getFollowing(@RequestParam(required = false) String name, HttpSession session) {
        List<UserSearchDto> following = profileService.getFollowing(name, session);
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




    // ========== SOCIALS =========

    @PostMapping("/socials")
    public ResponseEntity<Void> addSocialAccount(@RequestBody SocialAccountDto account, HttpSession session) {
        profileService.addSocialAccount(account, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/socials/{socialId}")
    public ResponseEntity<Void> removeSocialAccount(@PathVariable Long socialId, HttpSession session) {
        profileService.removeSocialAccount(socialId, session);
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

    @PostMapping("/show-rankings")
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
    public ResponseEntity<SeasonRankingReturnDto> addSeasonToRankingList(@RequestBody SeasonRankingDto season, HttpSession session) {
        SeasonRankingReturnDto seasonReturnDto = profileService.addSeasonToRankingList(season, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonReturnDto);
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
    public ResponseEntity<?> getCharacterRankingList(@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "type", required = false) String characterType, HttpSession session) {
        if (characterType == null) {
            return ResponseEntity.ok(profileService.getAllCharacterRankings(limit, session));
        } else {
            return ResponseEntity.ok(profileService.getCharacterRankingList(limit, characterType, session));
        }
    }

    @DeleteMapping("/character-rankings/{id}")
    public ResponseEntity<Void> removeFromCharacterRankingList(@PathVariable("id") String characterId, HttpSession session) {
        profileService.removeFromCharacterRankingList(characterId, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/character-rankings")
    public ResponseEntity<Void> updateCharacterRankingList(@RequestBody UpdateCharacterRankingsDto updates, HttpSession session) {
        profileService.updateCharacterRankingList(updates, session);
        return ResponseEntity.noContent().build();
    }




    // ========== CHARACTER DYNAMICS RANKING ==========
    @GetMapping("character-dynamics")
    public ResponseEntity<List<DynamicRankingReturnDto>> getDynamicsRankingList(@RequestParam(required = false) Integer limit, HttpSession session) {
        List<DynamicRankingReturnDto> dynamics = profileService.getDynamicsRankingList(limit, session);
        return ResponseEntity.ok(dynamics);
    }

    @PostMapping("character-dynamics")
    public ResponseEntity<DynamicRankingReturnDto> addDynamicToRankingList(@RequestBody DynamicRankingDto dynamic, HttpSession session) {
        DynamicRankingReturnDto newDynamic = profileService.addDynamicToRankingList(dynamic, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDynamic);
    }

    @DeleteMapping("character-dynamics/{id}")
    public ResponseEntity<Void> removeDynamicFromRankingList(@PathVariable Long id, HttpSession session) {
        profileService.removeDynamicFromRankingList(id, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("character-dynamics")
    public ResponseEntity<Void> updateDynamicRankingList(@RequestBody List<UpdateCharacterDynamicDto> updates, HttpSession session) {
        profileService.updateDynamicRankingList(updates, session);
        return ResponseEntity.noContent().build();
    }




    // ========== COLLECTIONS ==========

    @GetMapping("/collections")
    public ResponseEntity<List<CollectionDto>> getCollectionList(@RequestParam(required = false) String name, HttpSession session) {
        List<CollectionDto> collections = profileService.getCollectionList(name, session);
        return ResponseEntity.ok(collections);
    }

    @PostMapping("/collections")
    public ResponseEntity<CollectionDto> createCollection(@RequestBody CreateCollectionDto collection, HttpSession session) {
        CollectionDto newCollection = profileService.createCollection(collection, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollection);
    }

    @DeleteMapping("/collections/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id, HttpSession session) {
        profileService.deleteCollection(id, session);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/collections/{id}")
    public ResponseEntity<Void> updateCollection(@PathVariable Long id, @RequestBody UpdateCollectionDto collection, HttpSession session) {
        profileService.updateCollection(id, collection, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<CollectionReturnDto> getCollectionDetails(@PathVariable Long id, HttpSession session) {
        CollectionReturnDto collection = profileService.getCollection(id, session);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/collections/{id}/shows")
    public ResponseEntity<Void> addShowToCollection(@PathVariable Long id, @RequestBody WatchSendDto show, HttpSession session) {
        profileService.addShowToCollection(id, show, session);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/collections/{collectionId}/shows/{showId}")
    public ResponseEntity<Void> removeShowFromCollection(@PathVariable Long collectionId, @PathVariable Long showId, HttpSession session) {
        profileService.removeShowFromCollection(collectionId, showId, session);
        return ResponseEntity.noContent().build();
    }
}
