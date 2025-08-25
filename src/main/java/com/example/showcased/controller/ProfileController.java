package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.service.FileService;
import com.example.showcased.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_REVIEWS_PAGE_SIZE = 5;

    public ProfileController(ProfileService profileService,
                             FileService fileService) {
        this.profileService = profileService;
        this.fileService = fileService;
    }

    // ========== PROFILE DETAILS ==========

    @GetMapping("/details")
    public ResponseEntity<ProfileDetailsDto> getProfileDetails() {
        ProfileDetailsDto profileDetails = profileService.getProfileDetails();
        return ResponseEntity.ok(profileDetails);
    }

    @PatchMapping("/details")
    public ResponseEntity<Void> updateProfileDetails(@RequestBody UpdateProfileDetailsDto update) {
        profileService.updateProfileDetails(update);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        profileService.changePassword(changePasswordDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/settings")
    public ResponseEntity<ProfileSettingsDto> getProfileSettings() {
        ProfileSettingsDto profileSettings = profileService.getProfileSettings();
        return ResponseEntity.ok(profileSettings);
    }

    @GetMapping("/activity")
    public ResponseEntity<Page<ActivityDto>> getProfileActivity(@RequestParam(required = false, defaultValue = "1") int page) {
        Page<ActivityDto> activity = profileService.getProfileActivity(page);
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadProfilePicture(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<ShowReviewDto>> getReviews(@PageableDefault(page = 1, size = DEFAULT_REVIEWS_PAGE_SIZE, sort = "reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ShowReviewDto> reviews = profileService.getReviews(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/show-reviews")
    public ResponseEntity<Page<ShowReviewDto>> getShowReviews(@PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE, sort ="reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ShowReviewDto> reviews = profileService.getShowReviews(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/episode-reviews")
    public ResponseEntity<Page<EpisodeReviewDto>> getEpisodeReviews(@PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE, sort ="reviewDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EpisodeReviewDto> reviews = profileService.getEpisodeReviews(pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/followers")
    public ResponseEntity<Page<UserSearchDto>> getFollowers(@RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> followers = profileService.getFollowers(name, pageable);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following")
    public ResponseEntity<Page<UserSearchDto>> getFollowing(@RequestParam(required = false) String name,
                                                            @PageableDefault(page = 1, size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<UserSearchDto> following = profileService.getFollowing(name, pageable);
        return ResponseEntity.ok(following);
    }

    @DeleteMapping("/followers/{id}")
    public ResponseEntity<Void> removeFollower(@PathVariable("id") Long id) {
        profileService.removeFollower(id);
        return ResponseEntity.noContent().build();
    }




    // ========== SOCIALS =========

    @PostMapping("/socials")
    public ResponseEntity<Void> addSocialAccounts(@RequestBody List<SocialAccountDto> accounts) {
        profileService.addSocialAccount(accounts);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/socials/{socialId}")
    public ResponseEntity<Void> removeSocialAccount(@PathVariable Long socialId) {
        profileService.removeSocialAccount(socialId);
        return ResponseEntity.noContent().build();
    }




    // ========= WATCHLIST ==========

    @PostMapping("/watchlist")
    public ResponseEntity<Void> addShowToWatchlist(@RequestBody WatchSendDto show) {
        profileService.addShowToWatchlist(show);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<WatchReturnDto>> getWatchlist(@RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchlist = profileService.getWatchlist(limit);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/watchlist/{id}")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable("id") String id) {
        profileService.removeFromWatchlist(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/watchlist/{id}")
    public ResponseEntity<Void> moveToWatchingList(@PathVariable("id") String id) {
        profileService.moveToWatchingList(id);
        return ResponseEntity.noContent().build();
    }


    // ========== CURRENTLY WATCHING ==========

    @PostMapping("/currently-watching")
    public ResponseEntity<Void> addShowToWatchingList(@RequestBody WatchSendDto show) {
        profileService.addShowToWatchingList(show);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/currently-watching")
    public ResponseEntity<List<WatchReturnDto>> getWatchingList(@RequestParam(value = "limit", required = false) Integer limit) {
        List<WatchReturnDto> watchlist = profileService.getWatchingList(limit);
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/currently-watching/{id}")
    public ResponseEntity<Void> removeFromWatchingList(@PathVariable("id") String id) {
        profileService.removeFromWatchingList(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/currently-watching/{id}")
    public ResponseEntity<Void> moveToRankingList(@PathVariable("id") String id) {
        profileService.moveToRankingList(id);
        return ResponseEntity.noContent().build();
    }


    // ========== SHOW RANKINGS ==========

    @PostMapping("/show-rankings")
    public ResponseEntity<Void> addShowToRankingList(@RequestBody WatchSendDto show) {
        profileService.addShowToRankingList(show);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/show-rankings")
    public ResponseEntity<List<RankingReturnDto>> getShowRankingList(@RequestParam(value = "limit", required = false) Integer limit) {
        List<RankingReturnDto> ranking = profileService.getShowRankingList(limit);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/show-rankings/{id}")
    public ResponseEntity<Void> removeFromShowRankingList(@PathVariable("id") String id) {
        profileService.removeFromShowRankingList(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/show-rankings")
    public ResponseEntity<Void> updateShowRankingList(@RequestBody List<UpdateShowRankingDto> shows) {
        profileService.updateShowRankingList(shows);
        return ResponseEntity.noContent().build();
    }


    // ========== EPISODE RANKINGS ==========

    @PostMapping("/episode-rankings")
    public ResponseEntity<Void> addEpisodeToRankingList(@RequestBody EpisodeRankingDto episode) {
        profileService.addEpisodeToRankingList(episode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/episode-rankings")
    public ResponseEntity<List<EpisodeRankingReturnDto>> getEpisodeRankingList(@RequestParam(value = "limit", required = false) Integer limit) {
        List<EpisodeRankingReturnDto> ranking = profileService.getEpisodeRankingList(limit);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/episode-rankings/{id}")
    public ResponseEntity<Void> removeFromEpisodeRankingList(@PathVariable("id") Long id) {
        profileService.removeFromEpisodeRankingList(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/episode-rankings")
    public ResponseEntity<Void> updateEpisodeRankingList(@RequestBody List<UpdateEpisodeRankingDto> episodes) {
        profileService.updateEpisodeRankingList(episodes);
        return ResponseEntity.noContent().build();
    }


    // ========== SEASON RANKINGS ==========

    @PostMapping("/season-rankings")
    public ResponseEntity<SeasonRankingReturnDto> addSeasonToRankingList(@RequestBody SeasonRankingDto season) {
        SeasonRankingReturnDto seasonReturnDto = profileService.addSeasonToRankingList(season);
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonReturnDto);
    }

    @GetMapping("/season-rankings")
    public ResponseEntity<List<SeasonRankingReturnDto>> getSeasonRankingList(@RequestParam(value = "limit", required = false) Integer limit) {
        List<SeasonRankingReturnDto> ranking = profileService.getSeasonRankingList(limit);
        return ResponseEntity.ok(ranking);
    }

    @DeleteMapping("/season-rankings/{id}")
    public ResponseEntity<Void> removeFromSeasonRankingList(@PathVariable("id") Long seasonId) {
        profileService.removeFromSeasonRankingList(seasonId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/season-rankings")
    public ResponseEntity<Void> updateSeasonRankingList(@RequestBody List<UpdateSeasonRankingDto> seasons) {
        profileService.updateSeasonRankingList(seasons);
        return ResponseEntity.noContent().build();
    }


    // ========== CHARACTER RANKINGS ==========

    @PostMapping("/character-rankings")
    public ResponseEntity<Void> addCharacterToRankingList(@RequestBody CharacterRankingDto character) {
        profileService.addCharacterToRankingList(character);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/character-rankings")
    public ResponseEntity<?> getCharacterRankingList(@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "type", required = false) String characterType) {
        if (characterType == null) {
            return ResponseEntity.ok(profileService.getAllCharacterRankings(limit));
        } else {
            return ResponseEntity.ok(profileService.getCharacterRankingList(limit, characterType));
        }
    }

    @DeleteMapping("/character-rankings/{id}")
    public ResponseEntity<Void> removeFromCharacterRankingList(@PathVariable("id") String characterId) {
        profileService.removeFromCharacterRankingList(characterId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/character-rankings")
    public ResponseEntity<Void> updateCharacterRankingList(@RequestBody UpdateCharacterRankingsDto updates) {
        profileService.updateCharacterRankingList(updates);
        return ResponseEntity.noContent().build();
    }




    // ========== CHARACTER DYNAMICS RANKING ==========
    @GetMapping("character-dynamics")
    public ResponseEntity<List<DynamicRankingReturnDto>> getDynamicsRankingList(@RequestParam(required = false) Integer limit) {
        List<DynamicRankingReturnDto> dynamics = profileService.getDynamicsRankingList(limit);
        return ResponseEntity.ok(dynamics);
    }

    @PostMapping("character-dynamics")
    public ResponseEntity<DynamicRankingReturnDto> addDynamicToRankingList(@RequestBody DynamicRankingDto dynamic) {
        DynamicRankingReturnDto newDynamic = profileService.addDynamicToRankingList(dynamic);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDynamic);
    }

    @DeleteMapping("character-dynamics/{id}")
    public ResponseEntity<Void> removeDynamicFromRankingList(@PathVariable Long id) {
        profileService.removeDynamicFromRankingList(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("character-dynamics")
    public ResponseEntity<Void> updateDynamicRankingList(@RequestBody List<UpdateCharacterDynamicDto> updates) {
        profileService.updateDynamicRankingList(updates);
        return ResponseEntity.noContent().build();
    }




    // ========== COLLECTIONS ==========

    @GetMapping("/collections")
    public ResponseEntity<Page<CollectionDto>> getCollectionList(@RequestParam(required = false) String name,
                                                                 @PageableDefault(page = 1, size = 2) Pageable pageable) {
        Page<CollectionDto> collections = profileService.getCollectionList(name, pageable);
        return ResponseEntity.ok(collections);
    }

    @PostMapping("/collections")
    public ResponseEntity<CollectionDto> createCollection(@RequestBody CreateCollectionDto collection) {
        CollectionDto newCollection = profileService.createCollection(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollection);
    }

    @DeleteMapping("/collections/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        profileService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/collections/{id}")
    public ResponseEntity<Void> updateCollection(@PathVariable Long id, @RequestBody UpdateCollectionDto collection) {
        profileService.updateCollection(id, collection);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<CollectionReturnDto> getCollectionDetails(@PathVariable Long id) {
        CollectionReturnDto collection = profileService.getCollection(id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/collections/{id}/shows")
    public ResponseEntity<Void> addShowToCollection(@PathVariable Long id, @RequestBody WatchSendDto show) {
        profileService.addShowToCollection(id, show);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/collections/{collectionId}/shows/{showId}")
    public ResponseEntity<Void> removeShowFromCollection(@PathVariable Long collectionId, @PathVariable Long showId) {
        profileService.removeShowFromCollection(collectionId, showId);
        return ResponseEntity.noContent().build();
    }
}
