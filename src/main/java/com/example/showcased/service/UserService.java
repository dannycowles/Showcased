package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.enums.ActivityType;
import com.example.showcased.exception.*;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final ShowRankingRepository showRankingRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchingRepository watchingRepository;
    private final int numTopEntries = 10;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ShowReviewRepository showReviewRepository;
    private final FollowersRepository followersRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final CharacterRankingRepository characterRankingRepository;
    private final String[] validCharacterTypes = {"protagonist", "deuteragonist", "antagonist", "tritagonist", "side"};
    private final CollectionRepository collectionsRepository;
    private final ShowsInCollectionRepository showsInCollectionRepository;
    private final LikedCollectionsRepository likedCollectionsRepository;
    private final UserSocialRepository userSocialRepository;
    private final EpisodeReviewRepository episodeReviewRepository;
    private final DynamicRankingRepository dynamicRankingRepository;
    private final ActivitiesRepository activitiesRepository;

    public UserService(ShowRankingRepository showRankingRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       UserRepository userRepository,
                       ModelMapper modelMapper,
                       ShowReviewRepository showReviewRepository,
                       FollowersRepository followersRepository,
                       SeasonRankingRepository seasonRankingRepository,
                       CharacterRankingRepository characterRankingRepository,
                       CollectionRepository collectionsRepository,
                       ShowsInCollectionRepository showsInCollectionRepository,
                       LikedCollectionsRepository likedCollectionsRepository,
                       UserSocialRepository userSocialRepository,
                       EpisodeReviewRepository episodeReviewRepository,
                       DynamicRankingRepository dynamicRankingRepository,
                       ActivitiesRepository activitiesRepository) {
        this.showRankingRepository = showRankingRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.showReviewRepository = showReviewRepository;
        this.followersRepository = followersRepository;
        this.seasonRankingRepository = seasonRankingRepository;
        this.characterRankingRepository = characterRankingRepository;
        this.collectionsRepository = collectionsRepository;
        this.showsInCollectionRepository = showsInCollectionRepository;
        this.likedCollectionsRepository = likedCollectionsRepository;
        this.userSocialRepository = userSocialRepository;
        this.episodeReviewRepository = episodeReviewRepository;
        this.dynamicRankingRepository = dynamicRankingRepository;
        this.activitiesRepository = activitiesRepository;
    }

    private void ensureUserExists(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    public List<UserSearchDto> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query).stream()
                .map(user -> modelMapper.map(user, UserSearchDto.class))
                .collect(Collectors.toList());
    }

    public ProfileDetailsDto getUserDetails(Long userId, HttpSession session) {
        // Throw exception if the user was not found by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        ProfileDetailsDto userDetails = new ProfileDetailsDto();
        UserHeaderDataDto headerData = modelMapper.map(user, UserHeaderDataDto.class);
        headerData.setNumFollowers(getFollowersCount(userId));
        headerData.setNumFollowing(getFollowingCount(userId));
        headerData.setSocialAccounts(getSocialAccounts(userId));

        // Check if the user is logged in, if so we check they are following this user
        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            headerData.setFollowing(followersRepository.existsByFollowerIdAndFollowingId(loggedInUserId, userId));
            headerData.setOwnProfile(loggedInUserId.equals(userId));
        }
        userDetails.setHeaderData(headerData);

        userDetails.setWatchlistTop(getUserWatchlist(userId, numTopEntries));
        if (watchlistRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setHasMoreWatchlist(true);
        }

        userDetails.setWatchingTop(getUserWatchingList(userId, numTopEntries));
        if (watchingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setHasMoreWatching(true);
        }

        userDetails.setShowRankingTop(getUserShowRankings(userId, numTopEntries));
        if (showRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setHasMoreShowRanking(true);
        }

        userDetails.setEpisodeRankingTop(getUserEpisodeRankings(userId, numTopEntries));
        if (episodeRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setHasMoreEpisodeRanking(true);
        }

        userDetails.setSeasonRankingTop(getUserSeasonRankings(userId, numTopEntries));
        if (seasonRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setHasMoreSeasonRanking(true);
        }

        userDetails.setShowReviews(getUserShowReviews(userId));
        userDetails.setEpisodeReviews(getUserEpisodeReviews(userId));
        userDetails.setCharacterRankings(getAllUserCharacterRankings(userId, numTopEntries));
        userDetails.setDynamicRankingTop(getUserDynamicRankings(userId, numTopEntries));
        return userDetails;
    }

    private List<SocialAccountReturnDto> getSocialAccounts(Long userId) {
        ensureUserExists(userId);
        return userSocialRepository.findByIdUserId(userId);
    }




    // If a limit was provided, use that, else retrieve the entire ranking list
    private Pageable getPageRequest(Integer limit) {
        if (limit != null) {
            return PageRequest.of(0, limit);
        } else {
            return Pageable.unpaged();
        }
    }

    public List<WatchReturnDto> getUserWatchlist(Long userId, Integer limit) {
        ensureUserExists(userId);
        return watchlistRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public List<WatchReturnDto> getUserWatchingList(Long userId, Integer limit) {
        ensureUserExists(userId);
        return watchingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public List<RankingReturnDto> getUserShowRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        return showRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public List<EpisodeRankingReturnDto> getUserEpisodeRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        return episodeRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public List<SeasonRankingReturnDto> getUserSeasonRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        return seasonRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public List<CharacterRankingReturnDto> getUserCharacterRankings(Long userId, Integer limit, String characterType) {
        ensureUserExists(userId);

        // Make sure that the requested character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(characterType)) {
            throw new InvalidCharacterType("Invalid character type: " + characterType);
        }

        return characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, getPageRequest(limit));
    }

    public AllCharacterRankingDto getAllUserCharacterRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        AllCharacterRankingDto rankings = new AllCharacterRankingDto();

        rankings.setProtagonists(getUserCharacterRankings(userId, limit, validCharacterTypes[0]));
        rankings.setDeuteragonists(getUserCharacterRankings(userId, limit, validCharacterTypes[1]));
        rankings.setAntagonists(getUserCharacterRankings(userId, limit, validCharacterTypes[2]));
        rankings.setTritagonists(getUserCharacterRankings(userId, limit, validCharacterTypes[3]));
        rankings.setSide(getUserCharacterRankings(userId, limit, validCharacterTypes[4]));
        return rankings;
    }




    public List<ShowReviewWithUserInfoDto> getUserShowReviews(Long userId) {
        ensureUserExists(userId);
        return showReviewRepository.findByUserId(userId);
    }

    public List<EpisodeReviewWithUserInfoDto> getUserEpisodeReviews(Long userId) {
        ensureUserExists(userId);
        return episodeReviewRepository.findByUserId(userId);
    }

    public void followUser(Long followId, HttpSession session) {
        ensureUserExists(followId);
        Long userId = (Long) session.getAttribute("user");

        // Preventive code for if user tries to follow themselves
        if (userId.equals(followId)) {
            throw new FollowSelfException("A user cannot follow themselves.");
        }

        Follower newFollower = new Follower();
        newFollower.setFollowerId(userId);
        newFollower.setFollowingId(followId);
        followersRepository.save(newFollower);

        // Add the follow event to the activity table
        Activity followEvent = new Activity();
        followEvent.setUserId(followId);
        followEvent.setActivityType(ActivityType.FOLLOW.getDbValue());
        followEvent.setExternalId(newFollower.getId());
        activitiesRepository.save(followEvent);
    }

    @Transactional
    public void unfollowUser(Long unfollowId, HttpSession session) {
        ensureUserExists(unfollowId);
        Long userId = (Long) session.getAttribute("user");

        Optional<Follower> followerOpt = followersRepository.findByFollowerIdAndFollowingId(userId, unfollowId);
        if (followerOpt.isPresent()) {
            Follower removeFollower = followerOpt.get();
            activitiesRepository.deleteByExternalIdAndActivityType(removeFollower.getId(), ActivityType.FOLLOW.getDbValue());
            followersRepository.delete(removeFollower);
        }
    }

    public List<UserSearchDto> getFollowers(Long userId, String name, HttpSession session) {
        ensureUserExists(userId);
        List<UserSearchDto> followers = (name != null)
                ? followersRepository.getFollowersByIdFollowingIdFiltered(userId, name)
                : followersRepository.getFollowersByIdFollowingId(userId);

        setFollowingFlags(followers, session);
        return followers;
    }

    private Long getFollowersCount(Long userId) {
        ensureUserExists(userId);
        return followersRepository.countByFollowingId(userId);
    }

    public List<UserSearchDto> getFollowing(Long userId, String name, HttpSession session) {
        ensureUserExists(userId);
        List<UserSearchDto> following = (name != null)
                ? followersRepository.getFollowingByIdFollowerIdFiltered(userId, name)
                : followersRepository.getFollowingByIdFollowerId(userId);

        setFollowingFlags(following, session);
        return following;
    }

    private Long getFollowingCount(Long userId) {
        ensureUserExists(userId);
        return followersRepository.countByFollowerId(userId);
    }

    private void setFollowingFlags(List<UserSearchDto> following, HttpSession session) {
        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            Set<Long> followedUsers = followersRepository.getFollowingIds(loggedInUserId);

            // Iterate through followers list and check if set contains that id
            following.forEach(user -> {
                user.setFollowing(followedUsers.contains(user.getId()));
                user.setOwnProfile(loggedInUserId.equals(user.getId()));
            });
        }
    }




    public List<CollectionDto> getCollections(String name, Long userId) {
        ensureUserExists(userId);

        // If a name is specified filter by that, else retrieve all collections
        if (name != null) {
            return collectionsRepository.findByUserIdAndPrivateCollectionFalseAndCollectionNameContainingIgnoreCase(userId, name).stream()
                    .map(collection -> modelMapper.map(collection, CollectionDto.class))
                    .collect(Collectors.toList());
        } else {
            return collectionsRepository.findByUserIdAndPrivateCollectionFalse(userId).stream()
                    .map(collection -> modelMapper.map(collection, CollectionDto.class))
                    .collect(Collectors.toList());
        }
    }

    public CollectionReturnDto getShowsInCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        if (collection.isPrivateCollection()) {
            throw new UnauthorizedCollectionAccessException("Collection is private");
        }

        CollectionReturnDto collectionReturn = modelMapper.map(collection, CollectionReturnDto.class);
        collectionReturn.setShows(showsInCollectionRepository.findByIdCollectionId(collectionId));

        // If the user is logged in, check if they have liked the collection
        if (userId != null) {
            collectionReturn.setLikedByUser(likedCollectionsRepository.existsByUserIdAndCollectionId(userId, collectionId));
        }
        return collectionReturn;
    }

    @Transactional
    public void likeCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<Collection> collectionOpt = collectionsRepository.findById(collectionId);
        if (collectionOpt.isEmpty()) {
            throw new CollectionNotFoundException("Collection not found with ID: " + collectionId);
        }
        Collection collection = collectionOpt.get();

        // Check if the user has already liked this collection
        if (likedCollectionsRepository.existsByUserIdAndCollectionId(userId, collectionId)) {
            throw new AlreadyLikedException("You have already liked this collection");
        }

        LikedCollection newLike = new LikedCollection();
        newLike.setCollectionId(collectionId);
        newLike.setUserId(userId);
        likedCollectionsRepository.save(newLike);
        collectionsRepository.incrementLikes(collectionId);

        // Add the like collection event to the activities table, except for liking own collection
        if (!collection.getUserId().equals(userId)) {
            Activity collectionEvent  = new Activity();
            collectionEvent.setUserId(collection.getUserId());
            collectionEvent.setActivityType(ActivityType.LIKE_COLLECTION.getDbValue());
            collectionEvent.setExternalId(newLike.getId());
            activitiesRepository.save(collectionEvent);
        }
    }

    @Transactional
    public void unlikeCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        if (collectionsRepository.existsById(collectionId)) {
            // Check to ensure the user has liked the collection
            Optional<LikedCollection> likedCollectionsOpt = likedCollectionsRepository.findByUserIdAndCollectionId(userId, collectionId);
            if (likedCollectionsOpt.isPresent()) {
                LikedCollection likedCollection = likedCollectionsOpt.get();

                likedCollectionsRepository.delete(likedCollection);
                collectionsRepository.decrementLikes(collectionId);
                activitiesRepository.deleteByExternalIdAndActivityType(likedCollection.getId(), ActivityType.LIKE_COLLECTION.getDbValue());
            } else {
                throw new HaventLikedException("You have not liked this collection");
            }
        } else {
            throw new CollectionNotFoundException("Collection not found with ID: " + collectionId);
        }
    }

    public List<DynamicRankingReturnDto> getUserDynamicRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        return dynamicRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }
}
