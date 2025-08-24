package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.entity.Collection;
import com.example.showcased.exception.*;
import com.example.showcased.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final WatchingRepository watchingRepository;
    private final ModelMapper modelMapper;
    private final ShowRankingRepository showRankingRepository;
    private final int numTopEntries = 10;
    private final int numActivities = 10;
    private final String[] validCharacterTypes = {"protagonist", "deuteragonist", "antagonist", "tritagonist", "side"};
    private final ShowReviewRepository showReviewRepository;
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final UserRepository userRepository;
    private final FollowersRepository followersRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final SeasonInfoRepository seasonInfoRepository;
    private final CharacterRankingRepository characterRankingRepository;
    private final CollectionRepository collectionsRepository;
    private final ShowsInCollectionRepository showsInCollectionRepository;
    private final LikedCollectionsRepository likedCollectionsRepository;
    private final UserSocialRepository userSocialRepository;
    private final CharacterInfoRepository characterInfoRepository;
    private final EpisodeReviewRepository episodeReviewRepository;
    private final DynamicRankingRepository dynamicRankingRepository;
    private final ShowService showService;
    private final ActivitiesRepository activitiesRepository;
    private final AuthService authService;

    public ProfileService(WatchlistRepository watchlistRepository,
                          ShowInfoRepository showInfoRepository,
                          ModelMapper modelMapper,
                          WatchingRepository watchingRepository,
                          ShowRankingRepository showRankingRepository,
                          ShowReviewRepository showReviewRepository,
                          EpisodeInfoRepository episodeInfoRepository,
                          EpisodeRankingRepository episodeRankingRepository,
                          UserRepository userRepository,
                          FollowersRepository followersRepository,
                          SeasonRankingRepository seasonRankingRepository,
                          SeasonInfoRepository seasonInfoRepository,
                          CharacterRankingRepository characterRankingRepository,
                          CollectionRepository collectionRepository,
                          ShowsInCollectionRepository showsInCollectionRepository,
                          LikedCollectionsRepository likedCollectionsRepository,
                          UserSocialRepository userSocialRepository,
                          CharacterInfoRepository characterInfoRepository,
                          EpisodeReviewRepository episodeReviewRepository,
                          DynamicRankingRepository dynamicRankingRepository,
                          ShowService showService,
                          ActivitiesRepository activitiesRepository,
                          AuthService authService) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.watchingRepository = watchingRepository;
        this.modelMapper = modelMapper;
        this.showRankingRepository = showRankingRepository;
        this.showReviewRepository = showReviewRepository;
        this.episodeInfoRepository = episodeInfoRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.userRepository = userRepository;
        this.followersRepository = followersRepository;
        this.seasonRankingRepository = seasonRankingRepository;
        this.seasonInfoRepository = seasonInfoRepository;
        this.characterRankingRepository = characterRankingRepository;
        this.collectionsRepository = collectionRepository;
        this.showsInCollectionRepository = showsInCollectionRepository;
        this.likedCollectionsRepository = likedCollectionsRepository;
        this.userSocialRepository = userSocialRepository;
        this.characterInfoRepository = characterInfoRepository;
        this.episodeReviewRepository = episodeReviewRepository;
        this.dynamicRankingRepository = dynamicRankingRepository;
        this.showService = showService;
        this.activitiesRepository = activitiesRepository;
        this.authService = authService;
    }

    /**
     * Whenever a user adds a show to any of their lists,
     * the show info table might need to be updated too
     */
    private void addToShowInfoRepository(WatchSendDto show) {
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfo.setTitle(show.getShowTitle());
            showInfoRepository.save(showInfo);
        }
    }



    public ProfileDetailsDto getProfileDetails() {
        User user = authService.retrieveUserFromJwt();

        // Special profile details model mapper that skips the set following column
        ModelMapper profileMapper = new ModelMapper();
        profileMapper.typeMap(User.class, UserHeaderDataDto.class).addMappings(mapper -> {
            mapper.skip(UserHeaderDataDto::setFollowing);
        });

        ProfileDetailsDto profileDetails = new ProfileDetailsDto();
        UserHeaderDataDto headerData = profileMapper.map(user, UserHeaderDataDto.class);
        headerData.setSocialAccounts(getSocialAccounts());
        headerData.setOwnProfile(true);
        profileDetails.setHeaderData(headerData);

        profileDetails.setWatchlistTop(getWatchlist(numTopEntries));
        profileDetails.setWatchingTop(getWatchingList(numTopEntries));
        profileDetails.setShowRankingTop(getShowRankingList(numTopEntries));
        profileDetails.setEpisodeRankingTop(getEpisodeRankingList(numTopEntries));
        profileDetails.setReviews(getReviews(PageRequest.of(1, numTopEntries)).getContent());
        profileDetails.setSeasonRankingTop(getSeasonRankingList(numTopEntries));
        profileDetails.setCharacterRankings(getAllCharacterRankings(numTopEntries));
        profileDetails.setDynamicRankingTop(getDynamicsRankingList(numTopEntries));
        return profileDetails;
    }

    public ProfileSettingsDto getProfileSettings() {
        User user = authService.retrieveUserFromJwt();
        ProfileSettingsDto profileSettings = modelMapper.map(user, ProfileSettingsDto.class);
        profileSettings.setSocialAccounts(getSocialAccounts());
        return profileSettings;
    }

    public void updateProfileDetails(UpdateProfileDetailsDto update) {
        User user = authService.retrieveUserFromJwt();
        user.setBio(update.getBio());
        userRepository.save(user);
    }

    public void addShowToWatchlist(WatchSendDto show) {
        User user = authService.retrieveUserFromJwt();
        addToShowInfoRepository(show);

        // Check if the show is already in the user's watchlist, if so we throw an exception
        WatchId watchId = new WatchId(user.getId(), show.getShowId());
        if (watchlistRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You already have " + show.getShowTitle() + " on your watchlist");
        }

        // If the show is already on either the watching list or ranking list throw an exception
        if (watchingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your watching list");
        }

        if (showRankingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your ranking list");
        }

        Watchlist watchlistEntry = new Watchlist(watchId);
        watchlistRepository.save(watchlistEntry);
    }

    // If a limit was provided, use that, else retrieve the entire ranking list
    private Pageable getPageRequest(Integer limit) {
        if (limit != null) {
            return PageRequest.of(0, limit);
        } else {
            return Pageable.unpaged();
        }
    }

    public List<WatchReturnDto> getWatchlist(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return watchlistRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public void removeFromWatchlist(String id) {
        User user = authService.retrieveUserFromJwt();
        watchlistRepository.deleteById(new WatchId(user.getId(), Long.valueOf(id)));
    }

    public void moveToWatchingList(String id) {
        User user = authService.retrieveUserFromJwt();
        // First we need to delete the show from the user's watchlist
        watchlistRepository.deleteById(new WatchId(user.getId(), Long.valueOf(id)));

        // Then, we add the show to the currently watching list
        Watching watching = new Watching();
        watching.setId(new WatchId(user.getId(), Long.valueOf(id)));
        watchingRepository.save(watching);
    }




    @Transactional
    public void addSocialAccount(List<SocialAccountDto> accounts) {
        for (SocialAccountDto account : accounts) {
            User user =  authService.retrieveUserFromJwt();
            UserSocial socialAccount = new UserSocial();
            socialAccount.setId(new UserSocialId(user.getId(), account.getSocialId()));
            socialAccount.setHandle(account.getHandle());
            userSocialRepository.save(socialAccount);
        }
    }

    @Transactional
    public void removeSocialAccount(Long socialId) {
        User user =  authService.retrieveUserFromJwt();
        userSocialRepository.deleteById(new UserSocialId(user.getId(), socialId));
    }

    private List<SocialAccountReturnDto> getSocialAccounts() {
        User user = authService.retrieveUserFromJwt();
        return userSocialRepository.findByUsername(user.getDisplayName());
    }






    public void addShowToWatchingList(WatchSendDto show) {
        User user = authService.retrieveUserFromJwt();
        addToShowInfoRepository(show);

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        WatchId watchId = new WatchId(user.getId(), show.getShowId());
        if (watchingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You already have " + show.getShowTitle() + " on your watching list");
        }

        // If the show is already on the watchlist or ranking list, throw an exception
        if (watchlistRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your watchlist");
        }

        if (showRankingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your ranking list");
        }

        Watching watchingEntry = new Watching(watchId);
        watchingRepository.save(watchingEntry);
    }

    public List<WatchReturnDto> getWatchingList(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return watchingRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public void removeFromWatchingList(String id) {
        User user = authService.retrieveUserFromJwt();
        watchingRepository.deleteById(new WatchId(user.getId(), Long.valueOf(id)));
    }

    public void moveToRankingList(String id) {
        User user = authService.retrieveUserFromJwt();
        // First we need to delete the show from the user's currently watching list
        watchingRepository.deleteById(new WatchId(user.getId(), Long.valueOf(id)));

        // Then, we add the show to the user's ranking list
        // Check if the user's ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId(user.getId());
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId(user.getId(), Long.valueOf(id)));
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        showRankingRepository.save(ranking);
    }




    public void addShowToRankingList(WatchSendDto show) {
        User user = authService.retrieveUserFromJwt();
        addToShowInfoRepository(show);

        // Check if the show is already in the user's show ranking list, if so we throw an exception
        WatchId watchId = new WatchId(user.getId(), show.getShowId());
        if (showRankingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You already have " + show.getShowTitle() + " on your ranking list");
        }

        // If the show is already on the watching list or watchlist, throw an exception
        if (watchingRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your watching list");
        }

        if (watchlistRepository.existsById(watchId)) {
            throw new AlreadyOnListException("You have " + show.getShowTitle() + " currently on your watchlist");
        }

        // Check if the user's show ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId(user.getId());
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId(user.getId(), show.getShowId()));
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        showRankingRepository.save(ranking);
    }

    public List<RankingReturnDto> getShowRankingList(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return showRankingRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public void removeFromShowRankingList(String id) {
        User user = authService.retrieveUserFromJwt();
        showRankingRepository.deleteById(new WatchId(user.getId(), Long.valueOf(id)));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<RankingReturnDto> rankings = showRankingRepository.findByUsername(user.getDisplayName(), Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            ShowRanking ranking = new ShowRanking();
            ranking.setId(new WatchId(user.getId(), rankings.get(i).getShowId()));
            ranking.setRankNum(i + 1L);
            showRankingRepository.save(ranking);
        }
    }

    public void updateShowRankingList(List<UpdateShowRankingDto> shows) {
        User user = authService.retrieveUserFromJwt();
        for (UpdateShowRankingDto show : shows) {
            ShowRanking newRanking = modelMapper.map(show, ShowRanking.class);
            newRanking.getId().setUserId(user.getId());
            showRankingRepository.save(newRanking);
        }
    }




    public void addEpisodeToRankingList(EpisodeRankingDto episode) {
        User user = authService.retrieveUserFromJwt();
        EpisodeRanking ranking = new EpisodeRanking();
        EpisodeRankingId rankingId = new EpisodeRankingId(user.getId(), episode.getEpisodeId());
        ranking.setId(rankingId);

        // If the episode doesn't exist in the episode info table already we add it for easy access
        if (!episodeInfoRepository.existsById(episode.getEpisodeId())) {
            EpisodeInfo episodeInfo = new EpisodeInfo();
            episodeInfo.setId(episode.getEpisodeId());
            episodeInfo.setShowId(episode.getShowId());
            episodeInfo.setShowTitle(episode.getShowTitle());
            episodeInfo.setEpisodeTitle(episode.getEpisodeTitle());
            episodeInfo.setPosterPath(episode.getPosterPath());
            episodeInfo.setSeason(episode.getSeason());
            episodeInfo.setEpisode(episode.getEpisode());

            episodeInfoRepository.save(episodeInfo);
        }

        // Check if the show is already in the user's episode ranking list, if so we throw an exception
        if (episodeRankingRepository.existsById(rankingId)) {
            throw new AlreadyOnListException("Episode is already on ranking list");
        }

        // Check if the user's episode ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = episodeRankingRepository.findMaxRankNumByUserId(user.getId());
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        episodeRankingRepository.save(ranking);
    }

    public List<EpisodeRankingReturnDto> getEpisodeRankingList(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return episodeRankingRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public void removeFromEpisodeRankingList(Long episodeId) {
        User user = authService.retrieveUserFromJwt();
        episodeRankingRepository.deleteById(new EpisodeRankingId(user.getId(), episodeId));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<EpisodeRankingReturnDto> rankings = episodeRankingRepository.findByUsername(user.getDisplayName(), Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            EpisodeRanking ranking = new EpisodeRanking();
            ranking.setId(new EpisodeRankingId(user.getId(), rankings.get(i).getEpisodeId()));
            ranking.setRankNum(i + 1L);
            episodeRankingRepository.save(ranking);
        }
    }

    public void updateEpisodeRankingList(List<UpdateEpisodeRankingDto> episodes) {
        User user = authService.retrieveUserFromJwt();
        for (UpdateEpisodeRankingDto episode : episodes) {
            EpisodeRanking newRanking = new EpisodeRanking();
            newRanking.setId(new EpisodeRankingId(user.getId(), episode.getId()));
            newRanking.setRankNum(episode.getRankNum());
            episodeRankingRepository.save(newRanking);
        }
    }




    public SeasonRankingReturnDto addSeasonToRankingList(SeasonRankingDto season) {
        User user = authService.retrieveUserFromJwt();

        // Retrieve additional needed season information
        SeasonPartialDto seasonDetails = showService.getSeasonPartialDetails(season.getShowId(), season.getSeason());
        seasonDetails.setShowId(season.getShowId());
        seasonDetails.setShowTitle(season.getShowTitle());

        SeasonRanking ranking = new SeasonRanking();
        SeasonRankingId rankingId = new SeasonRankingId(user.getId(), seasonDetails.getId());
        ranking.setId(rankingId);

        // If season doesn't exist in season info table add it
        if (!seasonInfoRepository.existsById(seasonDetails.getId())) {
            SeasonInfo seasonInfo = modelMapper.map(seasonDetails, SeasonInfo.class);
            seasonInfoRepository.save(seasonInfo);
        }

        // If the season is already on the user's ranking list throw exception
        if (seasonRankingRepository.existsById(rankingId)) {
            throw new AlreadyOnListException("Season is already on ranking list");
        }

        // If user's ranking list is empty, rank will start at 1 else append to end
        Integer maxRank = seasonRankingRepository.findMaxRankNumByUserId(user.getId());
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum(maxRank + 1L);
        }
        seasonRankingRepository.save(ranking);

        SeasonRankingReturnDto seasonReturnDto = modelMapper.map(seasonDetails, SeasonRankingReturnDto.class);
        seasonReturnDto.setRankNum(ranking.getRankNum());
        return seasonReturnDto;
    }

    public List<SeasonRankingReturnDto> getSeasonRankingList(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return seasonRankingRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public void removeFromSeasonRankingList(Long seasonId) {
        User user = authService.retrieveUserFromJwt();
        seasonRankingRepository.deleteById(new SeasonRankingId(user.getId(), seasonId));

        // After deleting the season we need to adjust all the rank numbers to account for it
        List<SeasonRankingReturnDto> rankings = seasonRankingRepository.findByUsername(user.getDisplayName(), Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            SeasonRanking newRanking = new SeasonRanking();
            newRanking.setId(new SeasonRankingId(user.getId(), rankings.get(i).getId()));
            newRanking.setRankNum(i + 1L);
            seasonRankingRepository.save(newRanking);
        }
    }

    public void updateSeasonRankingList(List<UpdateSeasonRankingDto> seasons) {
        User user = authService.retrieveUserFromJwt();
        seasons.forEach(season -> {
            SeasonRanking newRanking = modelMapper.map(season, SeasonRanking.class);
            newRanking.setId(new SeasonRankingId(user.getId(), season.getId()));
            newRanking.setRankNum(season.getRankNum());
            seasonRankingRepository.save(newRanking);
        });
    }





    public void addCharacterToRankingList(CharacterRankingDto character) {
        User user = authService.retrieveUserFromJwt();

        // Check to ensure the character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(character.getCharacterType())) {
            throw new InvalidCharacterType("Invalid character type: " + character.getCharacterType());
        }

        // Check if the character already exists in the character info table, if not add it
        if (!characterInfoRepository.existsById(character.getCharacterId())) {
            CharacterInfo characterInfo = modelMapper.map(character, CharacterInfo.class);
            characterInfo.setId(character.getCharacterId());
            characterInfo.setName(character.getCharacterName());
            characterInfoRepository.save(characterInfo);
        }

        // Check if the show already exists in the show info table, if not add it
        if (!showInfoRepository.existsById(character.getShowId())) {
            ShowInfo showInfo = modelMapper.map(character, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }

        CharacterRanking ranking = new CharacterRanking();
        ranking.setId(new CharacterRankingId(user.getId(), character.getCharacterId()));
        ranking.setCharacterType(character.getCharacterType());

        // Check if the character is already on the user's list
        if (characterRankingRepository.existsById(ranking.getId())) {
            throw new AlreadyOnListException("Character is already on ranking list");
        }

        // If user's ranking list for that type is empty, start at 1 else append to end
        Integer maxRank = characterRankingRepository.findMaxRankNumByCharacterType(user.getId(), character.getCharacterType());
        if (maxRank != null) {
            ranking.setRankNum(maxRank + 1);
        } else {
            ranking.setRankNum(1);
        }
        characterRankingRepository.save(ranking);
    }

    public List<CharacterRankingReturnDto> getCharacterRankingList(Integer limit, String characterType) {
        User user = authService.retrieveUserFromJwt();

        // Check to ensure requested character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(characterType)) {
            throw new InvalidCharacterType("Invalid character type: " + characterType);
        }

        return characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), characterType, getPageRequest(limit));
    }

    public AllCharacterRankingDto getAllCharacterRankings(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        AllCharacterRankingDto rankings = new AllCharacterRankingDto();

        rankings.setProtagonists(characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), validCharacterTypes[0], getPageRequest(limit)));
        rankings.setDeuteragonists(characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), validCharacterTypes[1], getPageRequest(limit)));
        rankings.setAntagonists(characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), validCharacterTypes[2], getPageRequest(limit)));
        rankings.setTritagonists(characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), validCharacterTypes[3], getPageRequest(limit)));
        rankings.setSide(characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), validCharacterTypes[4], getPageRequest(limit)));
        return rankings;
    }

    public void removeFromCharacterRankingList(String characterId) {
        User user = authService.retrieveUserFromJwt();

        Optional<CharacterRanking> characterDelete = characterRankingRepository.findById(new CharacterRankingId(user.getId(), characterId));
        if (characterDelete.isPresent()) {
            String characterType = characterDelete.get().getCharacterType();
            characterRankingRepository.delete(characterDelete.get());

            // After deleting the character, we need to update the ranks of the other characters on list
            List<CharacterRankingReturnDto> rankings = characterRankingRepository.findByUsernameAndCharacterType(user.getDisplayName(), characterType, Pageable.unpaged());
            for (int i = 0; i < rankings.size(); i++) {
                CharacterRanking newRanking = new CharacterRanking();
                newRanking.setId(new CharacterRankingId(user.getId(), rankings.get(i).getId()));
                newRanking.setRankNum(i + 1);
                newRanking.setCharacterType(characterType);
                characterRankingRepository.save(newRanking);
            }
        }
    }

    public void updateCharacterRankingList(UpdateCharacterRankingsDto updates) {
        User user = authService.retrieveUserFromJwt();

        // Check to ensure character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(updates.getCharacterType())) {
            throw new InvalidCharacterType("Invalid character type: " + updates.getCharacterType());
        }

        // Update rankings
        updates.getUpdates().forEach( update -> {
            CharacterRanking newRanking = new CharacterRanking();
            newRanking.setId(new CharacterRankingId(user.getId(), update.getId()));
            newRanking.setRankNum(update.getRankNum());
            newRanking.setCharacterType(updates.getCharacterType());
            characterRankingRepository.save(newRanking);
        });
    }




    public Page<ShowReviewDto> getReviews(Pageable pageable) {
        User user = authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );

        List<ShowReviewDto> topShowReviews = showReviewRepository.findByUsername(user.getDisplayName(), user.getId(), Pageable.unpaged()).getContent();
        List<EpisodeReviewDto> topEpisodeReviews = episodeReviewRepository.findByUsername(user.getDisplayName(), user.getId(), Pageable.unpaged()).getContent();

        Sort sort = modifiedPage.getSort();
        Comparator<ShowReviewDto> comparator;

        // If no sort is provided, default to newest reviews first
        if (sort.isUnsorted()) {
            comparator = Comparator.comparing(ShowReviewDto::getReviewDate).reversed();
        } else {
            Sort.Order sortOrder = modifiedPage.getSort().iterator().next();
            String sortOrderRequest = sortOrder.getProperty() + "," + sortOrder.getDirection();

            comparator = switch(sortOrderRequest) {
                case "reviewDate,DESC" -> Comparator.comparing(ShowReviewDto::getReviewDate).reversed();
                case "rating,DESC" -> Comparator.comparing(ShowReviewDto::getRating).reversed();
                case "rating,ASC"  -> Comparator.comparing(ShowReviewDto::getRating);
                case "numLikes,DESC" -> Comparator.comparing(ShowReviewDto::getNumLikes).reversed();
                case "numLikes,ASC"  -> Comparator.comparing(ShowReviewDto::getNumLikes);
                default -> throw new IllegalStateException("Unexpected sort value: " + sortOrderRequest);
            };
        }

        List<ShowReviewDto> combined = Stream.concat(topShowReviews.stream(), topEpisodeReviews.stream())
                .sorted(comparator)
                .skip((long) modifiedPage.getPageNumber() * modifiedPage.getPageSize())
                .limit(modifiedPage.getPageSize())
                .toList();

        return new PageImpl<>(combined, modifiedPage, topShowReviews.size() +  topEpisodeReviews.size());
    }

    public Page<ShowReviewDto> getShowReviews(Pageable pageable) {
        User user =  authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );
        return showReviewRepository.findByUsername(user.getDisplayName(), user.getId(), modifiedPage);
    }

    public Page<EpisodeReviewDto> getEpisodeReviews(Pageable pageable) {
        User user =  authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );
        return episodeReviewRepository.findByUsername(user.getDisplayName(), user.getId(), modifiedPage);
    }

    public Page<UserSearchDto> getFollowers(String name, Pageable pageable) {
        User user =  authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize()
        );

        if (name != null) {
            return followersRepository.getFollowersByUsernameFiltered(user.getDisplayName(), name, modifiedPage);
        } else {
            return followersRepository.getFollowersByUsername(user.getDisplayName(), modifiedPage);
        }
    }

    public Page<UserSearchDto> getFollowing(String name, Pageable pageable) {
        User user =  authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize()
        );

        if (name != null) {
            return followersRepository.getFollowingByUsernameFiltered(user.getDisplayName(), name, modifiedPage);
        } else {
            return followersRepository.getFollowingByUsername(user.getDisplayName(), modifiedPage);
        }
    }

    @Transactional
    public void removeFollower(Long removeId) {
        User user =  authService.retrieveUserFromJwt();
        Follower removeFollower = new Follower();
        removeFollower.setFollowerId(removeId);
        removeFollower.setFollowingId(user.getId());
        followersRepository.delete(removeFollower);

        // Decrement the number of followers for the person completing the action, and the number of people following for the user being removed
        userRepository.decrementFollowersCount(user.getId());
        userRepository.decrementFollowingCount(removeId);
    }



    public Page<CollectionDto> getCollectionList(String name, Pageable pageable) {
        User user = authService.retrieveUserFromJwt();

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                Math.min(pageable.getPageNumber() - 1, 0),
                pageable.getPageSize(),
                pageable.getSort()
        );

        // If a name is specified filter by that, else retrieve all collections
        Page<Object[]> collectionObjects;
        if (name != null) {
            collectionObjects = collectionsRepository.findByUserIdAndCollectionNameContainingIgnoreCase(user.getId(), name, modifiedPage);
        } else {
            collectionObjects = collectionsRepository.findByUserId(user.getId(), modifiedPage);
        }

        List<CollectionDto> collections = new ArrayList<>();
        for (Object[] row : collectionObjects) {
            CollectionDto collection = new CollectionDto(
                    ((Integer) row[0]).longValue(),
                    ((Integer) row[1]).longValue(),
                    (String) row[2],
                    (Boolean) row[3],
                    (String) row[4],
                    ((Long) row[5]).intValue(),
                    (int) row[6]);
            collections.add(collection);
        }
        return new PageImpl<>(collections, pageable, collectionObjects.getTotalElements());
    }

    public CollectionDto createCollection(CreateCollectionDto collectionDto) {
        User user = authService.retrieveUserFromJwt();

        // Check to make sure user doesn't have a collection with the provided name already
        if (collectionsRepository.existsByUserIdAndCollectionName(user.getId(), collectionDto.getCollectionName())) {
            throw new DuplicateCollectionNameException("You already have a collection named " + collectionDto.getCollectionName());
        }

        // Save new collection to database
        Collection newCollection = modelMapper.map(collectionDto, Collection.class);
        newCollection.setUserId(user.getId());
        collectionsRepository.save(newCollection);
        return modelMapper.map(newCollection, CollectionDto.class);
    }

    public void deleteCollection(Long collectionId) {
        User user = authService.retrieveUserFromJwt();

        // Check to ensure the collection being deleted is actually owned by the logged-in user
        if (!collectionsRepository.existsByUserIdAndCollectionId(user.getId(), collectionId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this collection");
        }
        collectionsRepository.deleteById(collectionId);
    }

    @Transactional
    public void updateCollection(Long collectionId, UpdateCollectionDto collection) {
        User user = authService.retrieveUserFromJwt();
        Collection updateCollection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being modified is actually owned by the logged-in user
        if (!updateCollection.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to modify this collection");
        }

        // Update columns as needed
        if (collection.getIsPrivate() != null) {
            updateCollection.setPrivateCollection(collection.getIsPrivate());
        }
        if (collection.getCollectionName() != null) {
            if (collectionsRepository.existsByUserIdAndCollectionName(user.getId(), collection.getCollectionName())) {
                throw new DuplicateCollectionNameException("You already have a collection named " + collection.getCollectionName());
            }
            updateCollection.setCollectionName(collection.getCollectionName());
        }
        if (collection.getDescription() != null) {
            updateCollection.setDescription(collection.getDescription());
        }

        // If ranked is toggled we need to do extra processing
        if (collection.getIsRanked() != null) {
            updateCollection.setRanked(collection.getIsRanked());

            // If the collection is ranked, negate all ranks in the collection temporarily, so that the unique constraint is not violate
            if (updateCollection.isRanked()) {
                showsInCollectionRepository.negateCollectionRanks(collectionId);
            }

            // Updates the ranks as necessary based on private or not
            List<UpdateCollectionRankingDto> updates = collection.getShows();
            if (updates != null && !updates.isEmpty()) {
                List<ShowsInCollection> updatedShows = new ArrayList<>();
                for (int i = 0; i < updates.size(); i++) {
                    ShowsInCollection show = new ShowsInCollection();
                    show.setId(new ShowsInCollectionId(collectionId, updates.get(i).getId()));
                    show.setRankNum(updateCollection.isRanked() ? i + 1L : null);
                    updatedShows.add(show);
                }
                showsInCollectionRepository.saveAll(updatedShows);
            }
        }
        collectionsRepository.save(updateCollection);
    }

    public CollectionReturnDto getCollection(Long collectionId) {
        User user = authService.retrieveUserFromJwt();
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being retrieved is actually owned by the logged-in user
        if (!collection.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to view this collection");
        }

        CollectionReturnDto collectionReturn = modelMapper.map(collection, CollectionReturnDto.class);
        collectionReturn.setShows(showsInCollectionRepository.findByIdCollectionId(collectionId));
        collectionReturn.setLikedByUser(likedCollectionsRepository.existsByUserIdAndCollectionId(user.getId(), collectionId));
        return collectionReturn;
    }

    public void addShowToCollection(Long collectionId, WatchSendDto show) {
        User user = authService.retrieveUserFromJwt();
        Collection updateCollection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being added to is actually owned by the logged-in user
        if (!updateCollection.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to add to this collection");
        }

        addToShowInfoRepository(show);
        ShowsInCollectionId showsInCollectionId = new ShowsInCollectionId(collectionId, show.getShowId());
        if (showsInCollectionRepository.existsById(showsInCollectionId)) {
            throw new AlreadyInCollectionException("You already have " + show.getShowTitle() + " in this collection");
        }

        ShowsInCollection newShow = new ShowsInCollection();
        newShow.setId(showsInCollectionId);
        // Check if the collection is ranked, if so we need extra processing
        if (updateCollection.isRanked()) {
            Long maxRank = showsInCollectionRepository.findMaxRankNumByIdCollectionId(collectionId);
            if (maxRank != null) {
                newShow.setRankNum(maxRank + 1);
            } else {
                newShow.setRankNum(1L);
            }
        }
        showsInCollectionRepository.save(newShow);
    }

    public void removeShowFromCollection(Long collectionId, Long showId) {
        User user = authService.retrieveUserFromJwt();
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being modified is actually owned by the logged-in user
        if (!collection.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to modify this collection");
        }

        showsInCollectionRepository.deleteById(new ShowsInCollectionId(collectionId, showId));

        // Check if the collection is ranked, if so we need extra processing
        if (collection.isRanked()) {
            List<ShowsInCollection> shows = showsInCollectionRepository.findByIdCollectionIdOrderByRankNumAsc(collectionId);
            for (int i = 0; i < shows.size(); i++) {
                shows.get(i).setRankNum(i + 1L);
            }
            showsInCollectionRepository.saveAll(shows);
        }
    }



    public List<DynamicRankingReturnDto> getDynamicsRankingList(Integer limit) {
        User user = authService.retrieveUserFromJwt();
        return dynamicRankingRepository.findByUsername(user.getDisplayName(), getPageRequest(limit));
    }

    public DynamicRankingReturnDto addDynamicToRankingList(DynamicRankingDto dynamic) {
        User user = authService.retrieveUserFromJwt();

        // Sort by character ID when checking existing of dynamic... (12, 31) is the same as (31, 12)
        record CharacterData(String id, String name) {}
        List<CharacterData> sortedCharacters = Stream.of(new CharacterData(dynamic.getCharacter1Id(), dynamic.getCharacter1Name()),
                new CharacterData(dynamic.getCharacter2Id(), dynamic.getCharacter2Name())
        ).sorted(Comparator.comparing(CharacterData::id)).toList();
        CharacterData character1 = sortedCharacters.get(0);
        CharacterData character2 = sortedCharacters.get(1);

        // Check to make sure that the characters are not the same
        if (Objects.equals(character1.id(), character2.id())) {
            throw new InvalidDynamicException("Characters in dynamic must be different");
        }

        // Check if the dynamic is already on the user's ranking list
        if (dynamicRankingRepository.existsByUserIdAndCharacter1IdAndCharacter2Id(user.getId(), character1.id(),  character2.id())) {
            throw new AlreadyOnListException("Dynamic is already on your ranking list");
        }

        // Now, if each character doesn't exist in database, add it
        if (!characterInfoRepository.existsById(character1.id())) {
            CharacterInfo character1Info = new CharacterInfo();
            character1Info.setId(character1.id());
            character1Info.setShowId(dynamic.getShowId());
            character1Info.setName(character1.name());
            characterInfoRepository.save(character1Info);
        }

        if (!characterInfoRepository.existsById(character2.id())) {
            CharacterInfo character2Info = new CharacterInfo();
            character2Info.setId(character2.id());
            character2Info.setShowId(dynamic.getShowId());
            character2Info.setName(character2.name());
            characterInfoRepository.save(character2Info);
        }

        // If show doesn't exist in database, add it
        if (!showInfoRepository.existsById(dynamic.getShowId())) {
            ShowInfo show  = new ShowInfo();
            show.setShowId(dynamic.getShowId());
            show.setTitle(dynamic.getShowTitle());
            show.setPosterPath(dynamic.getPosterPath());
            showInfoRepository.save(show);
        }

        DynamicRanking dynamicRanking = new DynamicRanking();
        dynamicRanking.setCharacter1Id(character1.id());
        dynamicRanking.setCharacter2Id(character2.id());
        dynamicRanking.setUserId(user.getId());
        Integer maxRank = dynamicRankingRepository.findMaxRankNumByUserId(user.getId());
        dynamicRanking.setRankNum(maxRank == null ?  1 : maxRank + 1);
        dynamicRankingRepository.save(dynamicRanking);

        return new DynamicRankingReturnDto(dynamicRanking.getId(), character1.id(), character1.name(), character2.id(), character2.name(), dynamic.getShowTitle(), dynamicRanking.getRankNum());
    }

    @Transactional
    public void removeDynamicFromRankingList(Long dynamicId) {
        User user = authService.retrieveUserFromJwt();
        DynamicRanking dynamic = dynamicRankingRepository.findById(dynamicId)
                .orElseThrow(() -> new ItemNotFoundException("Could not find a dynamic with id: " + dynamicId));

        // Check to make sure the user id's match aka this is the users dynamic ranking
        if (!Objects.equals(user.getId(), dynamic.getUserId())) {
            throw new UnauthorizedAccessException("You do not have permission to modify this dynamic");
        }
        dynamicRankingRepository.delete(dynamic);

        // Retrieve the ranking entries and update the rank number using index
        List<DynamicRanking> rankings = dynamicRankingRepository.findByUserIdOrderByRankNumAsc(user.getId());
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRankNum(i + 1);
        }
        dynamicRankingRepository.saveAll(rankings);
    }

    @Transactional
    public void updateDynamicRankingList(List<UpdateCharacterDynamicDto> updates) {
        User user = authService.retrieveUserFromJwt();

        // Create a set of all provided dynamic ids
        Set<Long> dynamicIds = updates.stream()
                .map(UpdateCharacterDynamicDto::getId)
                .collect(Collectors.toSet());
        List<DynamicRanking> dynamics = dynamicRankingRepository.findByIdIn(dynamicIds);

        // Check to ensure that each of the IDs in the updates dto exists in the database
        if (dynamics.size() != updates.size()) {
            throw new ItemNotFoundException("One or more dynamics could not be found");
        }

        // Map each of the update IDs to its rank number for easy access for entity updates
        Map<Long, Integer> newRanks = updates.stream()
                .collect(Collectors.toMap(UpdateCharacterDynamicDto::getId, UpdateCharacterDynamicDto::getRankNum));
        for (DynamicRanking dynamic : dynamics) {
            // Validate that the dynamic being updated belongs to the user
            if (!Objects.equals(user.getId(), dynamic.getUserId())) {
                throw new UnauthorizedAccessException("You do not have permission to modify this dynamic");
            }
            dynamic.setRankNum(newRanks.get(dynamic.getId()));
        }
        dynamicRankingRepository.saveAll(dynamics);
    }

    public Page<ActivityDto> getProfileActivity(int pageNum) {
        User user = authService.retrieveUserFromJwt();
        return activitiesRepository.findByUserId(user.getId(), PageRequest.of(pageNum - 1, numActivities));
    }
}
