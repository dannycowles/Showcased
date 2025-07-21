package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.entity.Collection;
import com.example.showcased.exception.*;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                          ShowService showService, ActivitiesRepository activitiesRepository) {
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

    public ProfileDetailsDto getProfileDetails(HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        ProfileDetailsDto profileDetails = new ProfileDetailsDto();
        UserHeaderDataDto headerData = modelMapper.map(user, UserHeaderDataDto.class);
        headerData.setNumFollowers(getFollowersCount(session));
        headerData.setNumFollowing(getFollowingCount(session));
        headerData.setSocialAccounts(getSocialAccounts(session));
        headerData.setOwnProfile(true);
        profileDetails.setHeaderData(headerData);

        profileDetails.setWatchlistTop(getWatchlist(numTopEntries, session));
        profileDetails.setWatchingTop(getWatchingList(numTopEntries, session));
        profileDetails.setShowRankingTop(getShowRankingList(numTopEntries, session));
        profileDetails.setEpisodeRankingTop(getEpisodeRankingList(numTopEntries, session));
        profileDetails.setShowReviews(getShowReviews(session));
        profileDetails.setEpisodeReviews(getEpisodeReviews(session));
        profileDetails.setSeasonRankingTop(getSeasonRankingList(numTopEntries, session));
        profileDetails.setCharacterRankings(getAllCharacterRankings(numTopEntries, session));
        profileDetails.setDynamicRankingTop(getDynamicsRankingList(numTopEntries, session));
        return profileDetails;
    }

    public void updateProfileDetails(UpdateBioDto update, HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setBio(update.getBio());
        userRepository.save(user);
    }

    public void addShowToWatchlist(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's watchlist, if so we throw an exception
        if (watchlistRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on watchlist");
        }
        watchlistRepository.save(modelMapper.map(show, Watchlist.class));
    }

    // If a limit was provided, use that, else retrieve the entire ranking list
    private Pageable getPageRequest(Integer limit) {
        if (limit != null) {
            return PageRequest.of(0, limit);
        } else {
            return Pageable.unpaged();
        }
    }

    public List<WatchReturnDto> getWatchlist(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return watchlistRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromWatchlist(String id, HttpSession session) {
        watchlistRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToWatchingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        // First we need to delete the show from the user's watchlist
        watchlistRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // Then, we add the show to the currently watching list
        Watching watching = new Watching();
        watching.setId(new WatchId(userId, Long.valueOf(id)));
        watchingRepository.save(watching);
    }




    public void addSocialAccount(SocialAccountDto account, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        UserSocial socialAccount = new UserSocial();
        socialAccount.setId(new UserSocialId(userId, account.getSocialId()));
        socialAccount.setHandle(account.getHandle());
        userSocialRepository.save(socialAccount);
    }

    public void removeSocialAccount(Long socialId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        userSocialRepository.deleteById(new UserSocialId(userId, socialId));
    }

    private List<SocialAccountReturnDto> getSocialAccounts(HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return userSocialRepository.findByIdUserId(userId);
    }






    public void addShowToWatchingList(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        if (watchingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on watching list");
        }
        watchingRepository.save(modelMapper.map(show, Watching.class));
    }

    public List<WatchReturnDto> getWatchingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return watchingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromWatchingList(String id, HttpSession session) {
        watchingRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToRankingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        // First we need to delete the show from the user's currently watching list
        watchingRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // Then, we add the show to the user's ranking list
        // Check if the user's ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId(userId);
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId(userId, Long.valueOf(id)));
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        showRankingRepository.save(ranking);
    }




    public void addShowToRankingList(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's show ranking list, if so we throw an exception
        if (showRankingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on ranking list");
        }

        // Check if the user's show ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId(show.getUserId());
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId(show.getUserId(), show.getShowId()));
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        showRankingRepository.save(ranking);
    }

    public List<RankingReturnDto> getShowRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromShowRankingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        showRankingRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<RankingReturnDto> rankings = showRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            ShowRanking ranking = new ShowRanking();
            ranking.setId(new WatchId(userId, rankings.get(i).getShowId()));
            ranking.setRankNum(i + 1L);
            showRankingRepository.save(ranking);
        }
    }

    public void updateShowRankingList(List<UpdateShowRankingDto> shows, HttpSession session) {
        for (UpdateShowRankingDto show : shows) {
            ShowRanking newRanking = modelMapper.map(show, ShowRanking.class);
            newRanking.getId().setUserId((Long) session.getAttribute("user"));
            showRankingRepository.save(newRanking);
        }
    }




    public void addEpisodeToRankingList(EpisodeRankingDto episode, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        EpisodeRanking ranking = new EpisodeRanking();
        EpisodeRankingId rankingId = new EpisodeRankingId(userId, episode.getEpisodeId());
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
        Integer maxRank = episodeRankingRepository.findMaxRankNumByUserId(userId);
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        episodeRankingRepository.save(ranking);
    }

    public List<EpisodeRankingReturnDto> getEpisodeRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return episodeRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromEpisodeRankingList(Long episodeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        episodeRankingRepository.deleteById(new EpisodeRankingId(userId, episodeId));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<EpisodeRankingReturnDto> rankings = episodeRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            EpisodeRanking ranking = new EpisodeRanking();
            ranking.setId(new EpisodeRankingId(userId, rankings.get(i).getEpisodeId()));
            ranking.setRankNum(i + 1L);
            episodeRankingRepository.save(ranking);
        }
    }

    public void updateEpisodeRankingList(List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        for (UpdateEpisodeRankingDto episode : episodes) {
            EpisodeRanking newRanking = new EpisodeRanking();
            newRanking.setId(new EpisodeRankingId(userId, episode.getId()));
            newRanking.setRankNum(episode.getRankNum());
            episodeRankingRepository.save(newRanking);
        }
    }




    public SeasonRankingReturnDto addSeasonToRankingList(SeasonRankingDto season, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Retrieve additional needed season information
        SeasonPartialDto seasonDetails = showService.getSeasonPartialDetails(season.getShowId(), season.getSeason());
        seasonDetails.setShowId(season.getShowId());
        seasonDetails.setShowTitle(season.getShowTitle());

        SeasonRanking ranking = new SeasonRanking();
        SeasonRankingId rankingId = new SeasonRankingId(userId, seasonDetails.getId());
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
        Integer maxRank = seasonRankingRepository.findMaxRankNumByUserId(userId);
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

    public List<SeasonRankingReturnDto> getSeasonRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return seasonRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromSeasonRankingList(Long seasonId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        seasonRankingRepository.deleteById(new SeasonRankingId(userId, seasonId));

        // After deleting the season we need to adjust all the rank numbers to account for it
        List<SeasonRankingReturnDto> rankings = seasonRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            SeasonRanking newRanking = new SeasonRanking();
            newRanking.setId(new SeasonRankingId(userId, rankings.get(i).getId()));
            newRanking.setRankNum(i + 1L);
            seasonRankingRepository.save(newRanking);
        }
    }

    public void updateSeasonRankingList(List<UpdateSeasonRankingDto> seasons, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        seasons.forEach(season -> {
            SeasonRanking newRanking = modelMapper.map(season, SeasonRanking.class);
            newRanking.setId(new SeasonRankingId(userId, season.getId()));
            newRanking.setRankNum(season.getRankNum());
            seasonRankingRepository.save(newRanking);
        });
    }





    public void addCharacterToRankingList(CharacterRankingDto character, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

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
        ranking.setId(new CharacterRankingId(userId, character.getCharacterId()));
        ranking.setCharacterType(character.getCharacterType());

        // Check if the character is already on the user's list
        if (characterRankingRepository.existsById(ranking.getId())) {
            throw new AlreadyOnListException("Character is already on ranking list");
        }

        // If user's ranking list for that type is empty, start at 1 else append to end
        Integer maxRank = characterRankingRepository.findMaxRankNumByCharacterType(userId, character.getCharacterType());
        if (maxRank != null) {
            ranking.setRankNum(maxRank + 1);
        } else {
            ranking.setRankNum(1);
        }
        characterRankingRepository.save(ranking);
    }

    public List<CharacterRankingReturnDto> getCharacterRankingList(Integer limit, String characterType, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure requested character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(characterType)) {
            throw new InvalidCharacterType("Invalid character type: " + characterType);
        }

        return characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, getPageRequest(limit));
    }

    public AllCharacterRankingDto getAllCharacterRankings(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        AllCharacterRankingDto rankings = new AllCharacterRankingDto();

        rankings.setProtagonists(characterRankingRepository.findByIdUserIdAndCharacterType(userId, validCharacterTypes[0], getPageRequest(limit)));
        rankings.setDeuteragonists(characterRankingRepository.findByIdUserIdAndCharacterType(userId, validCharacterTypes[1], getPageRequest(limit)));
        rankings.setAntagonists(characterRankingRepository.findByIdUserIdAndCharacterType(userId, validCharacterTypes[2], getPageRequest(limit)));
        rankings.setTritagonists(characterRankingRepository.findByIdUserIdAndCharacterType(userId, validCharacterTypes[3], getPageRequest(limit)));
        rankings.setSide(characterRankingRepository.findByIdUserIdAndCharacterType(userId, validCharacterTypes[4], getPageRequest(limit)));
        return rankings;
    }

    public void removeFromCharacterRankingList(String characterId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        Optional<CharacterRanking> characterDelete = characterRankingRepository.findById(new CharacterRankingId(userId, characterId));
        if (characterDelete.isPresent()) {
            String characterType = characterDelete.get().getCharacterType();
            characterRankingRepository.delete(characterDelete.get());

            // After deleting the character, we need to update the ranks of the other characters on list
            List<CharacterRankingReturnDto> rankings = characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, Pageable.unpaged());
            for (int i = 0; i < rankings.size(); i++) {
                CharacterRanking newRanking = new CharacterRanking();
                newRanking.setId(new CharacterRankingId(userId, rankings.get(i).getId()));
                newRanking.setRankNum(i + 1);
                newRanking.setCharacterType(characterType);
                characterRankingRepository.save(newRanking);
            }
        }
    }

    public void updateCharacterRankingList(UpdateCharacterRankingsDto updates, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(updates.getCharacterType())) {
            throw new InvalidCharacterType("Invalid character type: " + updates.getCharacterType());
        }

        // Update rankings
        updates.getUpdates().forEach( update -> {
            CharacterRanking newRanking = new CharacterRanking();
            newRanking.setId(new CharacterRankingId(userId, update.getId()));
            newRanking.setRankNum(update.getRankNum());
            newRanking.setCharacterType(updates.getCharacterType());
            characterRankingRepository.save(newRanking);
        });
    }




    public List<ShowReviewWithUserInfoDto> getShowReviews(HttpSession session) {
        return showReviewRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<EpisodeReviewWithUserInfoDto> getEpisodeReviews(HttpSession session) {
        return episodeReviewRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<UserSearchDto> getFollowers(String name, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (name != null) {
            return followersRepository.getFollowersByIdFollowingIdFiltered(userId, name);
        } else {
            return followersRepository.getFollowersByIdFollowingId(userId);
        }
    }

    public Long getFollowersCount(HttpSession session) {
        return followersRepository.countByFollowingId((Long) session.getAttribute("user"));
    }

    public List<UserSearchDto> getFollowing(String name, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (name != null) {
            return followersRepository.getFollowingByIdFollowerIdFiltered(userId, name);
        } else {
            return followersRepository.getFollowingByIdFollowerId(userId);
        }
    }

    public Long getFollowingCount(HttpSession session) {
        return followersRepository.countByFollowerId((Long) session.getAttribute("user"));
    }

    public void removeFollower(Long removeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Follower removeFollower = new Follower();
        removeFollower.setFollowerId(removeId);
        removeFollower.setFollowingId(userId);
        followersRepository.delete(removeFollower);
    }



    public List<CollectionDto> getCollectionList(String name, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // If a name is specified filter by that, else retrieve all collections
        if (name != null) {
            return collectionsRepository.findByUserIdAndCollectionNameContainingIgnoreCase(userId, name).stream()
                    .map(collection -> modelMapper.map(collection, CollectionDto.class))
                    .collect(Collectors.toList());
        } else {
            return collectionsRepository.findByUserId(userId).stream()
                    .map(collection -> modelMapper.map(collection, CollectionDto.class))
                    .collect(Collectors.toList());
        }
    }

    public CollectionDto createCollection(CreateCollectionDto collection, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to make sure user doesn't have a collection with the provided name already
        if (collectionsRepository.existsByUserIdAndCollectionName(userId, collection.getCollectionName())) {
            throw new DuplicateCollectionNameException("You already have a collection named " + collection.getCollectionName());
        }

        // Save new collection to database
        Collection newCollection = new Collection(userId, collection.getCollectionName());
        collectionsRepository.save(newCollection);
        return modelMapper.map(newCollection, CollectionDto.class);
    }

    public void deleteCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure the collection being deleted is actually owned by the logged-in user
        if (!collectionsRepository.existsByUserIdAndCollectionId(userId, collectionId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to delete this collection");
        }
        collectionsRepository.deleteById(collectionId);
    }

    @Transactional
    public void updateCollection(Long collectionId, UpdateCollectionDto collection, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Collection updateCollection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being modified is actually owned by the logged-in user
        if (!updateCollection.getUserId().equals(userId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to modify this collection");
        }

        // Update columns as needed
        if (collection.getIsPrivate() != null) {
            updateCollection.setPrivateCollection(collection.getIsPrivate());
        }
        if (collection.getCollectionName() != null) {
            if (collectionsRepository.existsByUserIdAndCollectionName(userId, collection.getCollectionName())) {
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

    public CollectionReturnDto getCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being retrieved is actually owned by the logged-in user
        if (!collection.getUserId().equals(userId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to view this collection");
        }

        CollectionReturnDto collectionReturn = modelMapper.map(collection, CollectionReturnDto.class);
        collectionReturn.setShows(showsInCollectionRepository.findByIdCollectionId(collectionId));
        collectionReturn.setLikedByUser(likedCollectionsRepository.existsByUserIdAndCollectionId(userId, collectionId));
        return collectionReturn;
    }

    public void addShowToCollection(Long collectionId, WatchSendDto show, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Collection updateCollection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being added to is actually owned by the logged-in user
        if (!updateCollection.getUserId().equals(userId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to add to this collection");
        }

        addToShowInfoRepository(show);
        ShowsInCollectionId showsInCollectionId = new ShowsInCollectionId(collectionId, show.getShowId());
        if (showsInCollectionRepository.existsById(showsInCollectionId)) {
            throw new AlreadyInCollectionException("Show is already in this collection");
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

    public void removeShowFromCollection(Long collectionId, Long showId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        // Check to ensure the collection being modified is actually owned by the logged-in user
        if (!collection.getUserId().equals(userId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to modify this collection");
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



    public List<DynamicRankingReturnDto> getDynamicsRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return dynamicRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public DynamicRankingReturnDto addDynamicToRankingList(DynamicRankingDto dynamic, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

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
        if (dynamicRankingRepository.existsByUserIdAndCharacter1IdAndCharacter2Id(userId, character1.id(),  character2.id())) {
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
        dynamicRanking.setUserId(userId);
        Integer maxRank = dynamicRankingRepository.findMaxRankNumByUserId(userId);
        dynamicRanking.setRankNum(maxRank == null ?  1 : maxRank + 1);
        dynamicRankingRepository.save(dynamicRanking);

        return new DynamicRankingReturnDto(dynamicRanking.getId(), character1.id(), character1.name(), character2.id(), character2.name(), dynamic.getShowTitle(), dynamicRanking.getRankNum());
    }

    @Transactional
    public void removeDynamicFromRankingList(Long dynamicId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        DynamicRanking dynamic = dynamicRankingRepository.findById(dynamicId)
                .orElseThrow(() -> new ItemNotFoundException("Could not find a dynamic with id: " + dynamicId));

        // Check to make sure the user id's match aka this is the users dynamic ranking
        if (!Objects.equals(userId, dynamic.getUserId())) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to modify this dynamic");
        }
        dynamicRankingRepository.delete(dynamic);

        // Retrieve the ranking entries and update the rank number using index
        List<DynamicRanking> rankings = dynamicRankingRepository.findByUserIdOrderByRankNumAsc(userId);
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRankNum(i + 1);
        }
        dynamicRankingRepository.saveAll(rankings);
    }

    @Transactional
    public void updateDynamicRankingList(List<UpdateCharacterDynamicDto> updates, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

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
            if (!Objects.equals(userId, dynamic.getUserId())) {
                throw new UnauthorizedCollectionAccessException("You do not have permission to modify this dynamic");
            }
            dynamic.setRankNum(newRanks.get(dynamic.getId()));
        }
        dynamicRankingRepository.saveAll(dynamics);
    }

    public List<ActivityDto> getProfileActivity(HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return activitiesRepository.findByUserId(userId);
    }
}
