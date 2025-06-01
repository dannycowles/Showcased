package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.exception.*;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final WatchingRepository watchingRepository;
    private final ModelMapper modelMapper;
    private final ShowRankingRepository showRankingRepository;
    private final int numTopEntries = 10;
    private final String[] validCharacterTypes = {"protagonist", "deuteragonist", "antagonist"};
    private final ReviewRepository reviewRepository;
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

    public ProfileService(WatchlistRepository watchlistRepository,
                          ShowInfoRepository showInfoRepository,
                          ModelMapper modelMapper,
                          WatchingRepository watchingRepository,
                          ShowRankingRepository showRankingRepository,
                          ReviewRepository reviewRepository,
                          EpisodeInfoRepository episodeInfoRepository,
                          EpisodeRankingRepository episodeRankingRepository,
                          UserRepository userRepository,
                          FollowersRepository followersRepository,
                          SeasonRankingRepository seasonRankingRepository,
                          SeasonInfoRepository seasonInfoRepository,
                          CharacterRankingRepository characterRankingRepository,
                          CollectionRepository collectionRepository,
                          ShowsInCollectionRepository showsInCollectionRepository,
                          LikedCollectionsRepository likedCollectionsRepository) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.watchingRepository = watchingRepository;
        this.modelMapper = modelMapper;
        this.showRankingRepository = showRankingRepository;
        this.reviewRepository = reviewRepository;
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
    }

    /**
     * Whenever a user adds a show to any of their lists,
     * the show info table might need to be updated too
     */
    private void addToShowInfoRepository(WatchSendDto show) {
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }
    }

    public ProfileDetailsDto getProfileDetails(HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        ProfileDetailsDto profileDetails = modelMapper.map(user, ProfileDetailsDto.class);
        profileDetails.setWatchlistTop(getWatchlist(numTopEntries, session));
        profileDetails.setWatchingTop(getWatchingList(numTopEntries, session));
        profileDetails.setShowRankingTop(getShowRankingList(numTopEntries, session));
        profileDetails.setEpisodeRankingTop(getEpisodeRankingList(numTopEntries, session));
        profileDetails.setReviews(getReviews(session));
        profileDetails.setNumFollowers(getFollowersCount(session));
        profileDetails.setNumFollowing(getFollowingCount(session));
        profileDetails.setSeasonRankingTop(getSeasonRankingList(numTopEntries, session));
        profileDetails.setCharacterRankings(getAllCharacterRankings(numTopEntries, session));
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
        EpisodeRankingId rankingId = new EpisodeRankingId(userId, episode.getId());
        ranking.setId(rankingId);

        // If the episode doesn't exist in the episode info table already we add it for easy access
        if (!episodeInfoRepository.existsById(episode.getId())) {
            EpisodeInfo episodeInfo = new EpisodeInfo();
            episodeInfo.setId(episode.getId());
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
            ranking.setId(new EpisodeRankingId(userId, rankings.get(i).getId()));
            ranking.setRankNum(i + 1L);
            episodeRankingRepository.save(ranking);
        }
    }

    public void updateEpisodeRankingList(List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        for (UpdateEpisodeRankingDto episode : episodes) {
            EpisodeRanking newRanking = new EpisodeRanking();
            newRanking.setId(new EpisodeRankingId(userId, episode.getEpisodeId()));
            newRanking.setRankNum(episode.getRankNum());
            episodeRankingRepository.save(newRanking);
        }
    }




    public void addSeasonToRankingList(SeasonRankingDto season, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        SeasonRanking ranking = new SeasonRanking();
        SeasonRankingId rankingId = new SeasonRankingId(userId, season.getId());
        ranking.setId(rankingId);

        // If season doesn't exist in season info table add it
        if (!seasonInfoRepository.existsById(season.getId())) {
            SeasonInfo seasonInfo = modelMapper.map(season, SeasonInfo.class);
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

        CharacterRanking ranking = modelMapper.map(character, CharacterRanking.class);
        CharacterRankingId rankingId = new CharacterRankingId(userId, character.getCharacterName());
        ranking.setId(rankingId);

        // Check if the character is already on the user's list
        if (characterRankingRepository.existsById(rankingId)) {
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
        return rankings;
    }

    public void removeFromCharacterRankingList(String characterType, String name, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        characterRankingRepository.deleteById(new CharacterRankingId(userId, name));

        // After deleting the character, we need to update the ranks of the other characters on list
        List<CharacterRankingReturnDto> rankings = characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            CharacterRanking newRanking = new CharacterRanking();
            newRanking.setId(new CharacterRankingId(userId, rankings.get(i).getCharacterName()));
            newRanking.setRankNum(i + 1);
            newRanking.setCharacterType(characterType);
            newRanking.setShowName(rankings.get(i).getShowName());
            characterRankingRepository.save(newRanking);
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
            newRanking.setId(new CharacterRankingId(userId, update.getCharacterName()));
            newRanking.setRankNum(update.getRankNum());
            newRanking.setShowName(update.getShowName());
            newRanking.setCharacterType(updates.getCharacterType());
            characterRankingRepository.save(newRanking);
        });
    }




    public List<ReviewWithUserInfoDto> getReviews(HttpSession session) {
        return reviewRepository.findByUserId((Long) session.getAttribute("user"));
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
        return followersRepository.countByIdFollowingId((Long) session.getAttribute("user"));
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
        return followersRepository.countByIdFollowerId((Long) session.getAttribute("user"));
    }

    public void removeFollower(Long removeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        followersRepository.deleteById(new FollowerId(removeId, userId));
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

    public void createCollection(CreateCollectionDto collection, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to make sure user doesn't have a collection with the provided name already
        if (collectionsRepository.existsByUserIdAndCollectionName(userId, collection.getCollectionName())) {
            throw new DuplicateCollectionNameException("You already have a collection named " + collection.getCollectionName());
        }

        // Save new collection to database
        Collection newCollection = new Collection(userId, collection.getCollectionName());
        collectionsRepository.save(newCollection);
    }

    public void deleteCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure the collection being deleted is actually owned by the logged-in user
        if (!collectionsRepository.existsByUserIdAndCollectionId(userId, collectionId)) {
            throw new UnauthorizedCollectionAccessException("You do not have permission to delete this collection");
        }
        collectionsRepository.deleteById(collectionId);
    }

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
            List<UpdateCollectionRankingDto> updates = collection.getShows();

            if (updates != null && !updates.isEmpty()) {
                List<ShowsInCollection> updatedShows = new ArrayList<>();
                for (int i = 0; i < updates.size(); i++) {
                    ShowsInCollection show = new ShowsInCollection();
                    show.setId(new ShowsInCollectionId(collectionId, updates.get(i).getShowId()));
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
        collectionReturn.setNumLikes(likedCollectionsRepository.countByIdCollectionId(collectionId));
        collectionReturn.setLikedByUser(likedCollectionsRepository.existsById(new LikedCollectionsId(userId, collectionId)));
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
}
