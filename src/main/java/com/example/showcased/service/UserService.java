package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.entity.Collection;
import com.example.showcased.enums.ActivityType;
import com.example.showcased.exception.*;
import com.example.showcased.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final AuthService authService;

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
                       ActivitiesRepository activitiesRepository,
                       AuthService authService) {
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
        this.authService = authService;
    }

    private void ensureUserExists(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void ensureUserExistsByUsername(String username) {
        // Check to make sure user exists in system
        if (!userRepository.existsByDisplayName(username)) {
            throw new UserNotFoundException(username);
        }
    }

    public List<UserSearchDto> searchUsers(String query) {
        ModelMapper searchMapper = new ModelMapper();
        searchMapper.typeMap(User.class, UserSearchDto.class).addMappings(mapper -> {
            mapper.skip(UserSearchDto::setFollowing);
        });
        return userRepository.findByDisplayNameContainingIgnoreCase(query).stream()
                .map(user -> searchMapper.map(user, UserSearchDto.class))
                .collect(Collectors.toList());
    }

    public ProfileDetailsDto getUserDetails(String username) {
        ensureUserExistsByUsername(username);
        User user = userRepository.findByDisplayName(username).get();

        // Special user details model mapper that skips the set following column
        ModelMapper userMapper = new ModelMapper();
        userMapper.typeMap(User.class, UserHeaderDataDto.class).addMappings(mapper -> {
            mapper.skip(UserHeaderDataDto::setFollowing);
        });

        ProfileDetailsDto userDetails = new ProfileDetailsDto();
        UserHeaderDataDto headerData = userMapper.map(user, UserHeaderDataDto.class);
        headerData.setSocialAccounts(getSocialAccounts(username));

        // Check if the user is logged in, if so we check they are following this user
        User loggedInUser = authService.retrieveUserFromJwt();
        if (loggedInUser != null) {
            headerData.setFollowing(followersRepository.existsByFollowerIdAndFollowingId(loggedInUser.getId(), user.getId()));
            headerData.setOwnProfile(loggedInUser.getId().equals(user.getId()));
        }
        userDetails.setHeaderData(headerData);

        userDetails.setWatchlistTop(getUserWatchlist(username, numTopEntries));
        if (watchlistRepository.countByUsername(username) > numTopEntries) {
            userDetails.setHasMoreWatchlist(true);
        }

        userDetails.setWatchingTop(getUserWatchingList(username, numTopEntries));
        if (watchingRepository.countByUsername(username) > numTopEntries) {
            userDetails.setHasMoreWatching(true);
        }

        userDetails.setShowRankingTop(getUserShowRankings(username, numTopEntries));
        if (showRankingRepository.countByUsername(username) > numTopEntries) {
            userDetails.setHasMoreShowRanking(true);
        }

        userDetails.setEpisodeRankingTop(getUserEpisodeRankings(username, numTopEntries));
        if (episodeRankingRepository.countByUsername(username) > numTopEntries) {
            userDetails.setHasMoreEpisodeRanking(true);
        }

        userDetails.setSeasonRankingTop(getUserSeasonRankings(username, numTopEntries));
        if (seasonRankingRepository.countByUsername(username) > numTopEntries) {
            userDetails.setHasMoreSeasonRanking(true);
        }

        userDetails.setReviews(getUserReviews(username, PageRequest.of(1, numTopEntries)).getContent());
        userDetails.setCharacterRankings(getAllUserCharacterRankings(username, numTopEntries));
        userDetails.setDynamicRankingTop(getUserDynamicRankings(username, numTopEntries));
        return userDetails;
    }

    private List<SocialAccountReturnDto> getSocialAccounts(String username) {
        ensureUserExistsByUsername(username);
        return userSocialRepository.findByUsername(username);
    }




    // If a limit was provided, use that, else retrieve the entire ranking list
    private Pageable getPageRequest(Integer limit) {
        if (limit != null) {
            return PageRequest.of(0, limit);
        } else {
            return Pageable.unpaged();
        }
    }

    public List<WatchReturnDto> getUserWatchlist(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return watchlistRepository.findByUsername(username, getPageRequest(limit));
    }

    public List<WatchReturnDto> getUserWatchingList(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return watchingRepository.findByUsername(username, getPageRequest(limit));
    }

    public List<RankingReturnDto> getUserShowRankings(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return showRankingRepository.findByUsername(username, getPageRequest(limit));
    }

    public List<EpisodeRankingReturnDto> getUserEpisodeRankings(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return episodeRankingRepository.findByUsername(username, getPageRequest(limit));
    }

    public List<SeasonRankingReturnDto> getUserSeasonRankings(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return seasonRankingRepository.findByUsername(username, getPageRequest(limit));
    }

    public List<CharacterRankingReturnDto> getUserCharacterRankings(String username, Integer limit, String characterType) {
        ensureUserExistsByUsername(username);

        // Make sure that the requested character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(characterType)) {
            throw new InvalidCharacterType("Invalid character type: " + characterType);
        }

        return characterRankingRepository.findByUsernameAndCharacterType(username, characterType, getPageRequest(limit));
    }

    public AllCharacterRankingDto getAllUserCharacterRankings(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        AllCharacterRankingDto rankings = new AllCharacterRankingDto();

        rankings.setProtagonists(getUserCharacterRankings(username, limit, validCharacterTypes[0]));
        rankings.setDeuteragonists(getUserCharacterRankings(username, limit, validCharacterTypes[1]));
        rankings.setAntagonists(getUserCharacterRankings(username, limit, validCharacterTypes[2]));
        rankings.setTritagonists(getUserCharacterRankings(username, limit, validCharacterTypes[3]));
        rankings.setSide(getUserCharacterRankings(username, limit, validCharacterTypes[4]));
        return rankings;
    }



    public Page<ShowReviewDto> getUserReviews(String username, Pageable pageable) {
        User loggedInUser = authService.retrieveUserFromJwt();
        Long loggedInUserId = (loggedInUser != null) ? loggedInUser.getId() : null;

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );

        List<ShowReviewDto> topShowReviews = showReviewRepository.findByUsername(username, loggedInUserId, Pageable.unpaged()).getContent();
        List<EpisodeReviewDto> topEpisodeReviews = episodeReviewRepository.findByUsername(username, loggedInUserId, Pageable.unpaged()).getContent();

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

    public Page<ShowReviewDto> getUserShowReviews(String username, Pageable pageable) {
        ensureUserExistsByUsername(username);
        User loggedInUser = authService.retrieveUserFromJwt();
        Long loggedInUserId = (loggedInUser != null) ? loggedInUser.getId() : null;

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );
        return showReviewRepository.findByUsername(username, loggedInUserId, modifiedPage);
    }

    public Page<EpisodeReviewDto> getUserEpisodeReviews(String username, Pageable pageable) {
        ensureUserExistsByUsername(username);
        User loggedInUser = authService.retrieveUserFromJwt();
        Long loggedInUserId = (loggedInUser != null) ? loggedInUser.getId() : null;

        // Subtract 1 from provided pageable to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                pageable.getSort()
        );
        return episodeReviewRepository.findByUsername(username, loggedInUserId, modifiedPage);
    }

    @Transactional
    public void followUser(Long followId) {
        ensureUserExists(followId);
        User loggedInUser = authService.retrieveUserFromJwt();

        // Preventive code for if user tries to follow themselves
        if (loggedInUser.getId().equals(followId)) {
            throw new FollowSelfException("A user cannot follow themselves.");
        }

        Follower newFollower = new Follower();
        newFollower.setFollowerId(loggedInUser.getId());
        newFollower.setFollowingId(followId);
        followersRepository.save(newFollower);

        // Add the follow event to the activity table
        Activity followEvent = new Activity();
        followEvent.setUserId(followId);
        followEvent.setActivityType(ActivityType.FOLLOW.getDbValue());
        followEvent.setExternalId(newFollower.getId());
        activitiesRepository.save(followEvent);

        // Increment the number of followers for the user being followed, and number of people following for the person completing the action
        userRepository.incrementFollowersCount(followId);
        userRepository.incrementFollowingCount(loggedInUser.getId());
    }

    @Transactional
    public void unfollowUser(Long unfollowId) {
        ensureUserExists(unfollowId);
        User loggedInUser = authService.retrieveUserFromJwt();

        Optional<Follower> followerOpt = followersRepository.findByFollowerIdAndFollowingId(loggedInUser.getId(), unfollowId);
        if (followerOpt.isPresent()) {
            Follower removeFollower = followerOpt.get();
            activitiesRepository.deleteByExternalIdAndActivityType(removeFollower.getId(), ActivityType.FOLLOW.getDbValue());
            followersRepository.delete(removeFollower);
        }

        // Decrement the number of followers for the user being unfollowed, and number of people following for the person completing the action
        userRepository.decrementFollowersCount(unfollowId);
        userRepository.decrementFollowingCount(loggedInUser.getId());
    }

    public Page<UserSearchDto> getFollowers(String username, String name, Pageable pageable) {
        ensureUserExistsByUsername(username);

        // Subtract 1 from page to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize()
        );

        Page<UserSearchDto> followers = (name != null)
                ? followersRepository.getFollowersByUsernameFiltered(username, name, modifiedPage)
                : followersRepository.getFollowersByUsername(username, modifiedPage);

        setFollowingFlags(followers.getContent());
        return followers;
    }

    public Page<UserSearchDto> getFollowing(String username, String name, Pageable pageable) {
        ensureUserExistsByUsername(username);

        // Subtract 1 from page to align with 0-index
        Pageable modifiedPage = PageRequest.of(
                pageable.getPageNumber() - 1,
                pageable.getPageSize()
        );

        Page<UserSearchDto> following = (name != null)
                ? followersRepository.getFollowingByUsernameFiltered(username, name, modifiedPage)
                : followersRepository.getFollowingByUsername(username, modifiedPage);

        setFollowingFlags(following.getContent());
        return following;
    }

    private void setFollowingFlags(List<UserSearchDto> following) {
        User loggedInUser = authService.retrieveUserFromJwt();
        Long loggedInUserId = (loggedInUser != null) ? loggedInUser.getId() : null;
        if (loggedInUserId != null) {
            Set<Long> followedUsers = followersRepository.getFollowingIds(loggedInUserId);

            // Iterate through followers list and check if set contains that id
            following.forEach(user -> {
                user.setFollowing(followedUsers.contains(user.getId()));
                user.setOwnProfile(loggedInUserId.equals(user.getId()));
            });
        }
    }




    public List<CollectionDto> getCollections(String name, String username) {
        ensureUserExistsByUsername(username);
        User user = userRepository.findByDisplayName(username).get();

        // If a name is specified filter by that, else retrieve all collections
        List<Object[]> collectionObjects;
        if (name != null) {
            collectionObjects = collectionsRepository.findByUserIdAndPrivateCollectionFalseAndCollectionNameContainingIgnoreCase(user.getId(), name);
        } else {
            collectionObjects = collectionsRepository.findByUserIdAndPrivateCollectionFalse(user.getId());
        }

        List<CollectionDto> collections = new ArrayList<>();
        for (Object[] row : collectionObjects) {
            CollectionDto collection = new CollectionDto(
                    ((Integer) row[0]).longValue(),
                    ((Integer) row[1]).longValue(),
                    (String) row[2],
                    (Boolean) row[3],
                    (String) row[4],
                    ((Long) row[5]).intValue());
            collections.add(collection);
        }
        return collections;
    }

    public CollectionReturnDto getShowsInCollection(Long collectionId) {
        User loggedInUser = authService.retrieveUserFromJwt();

        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        if (collection.isPrivateCollection()) {
            throw new UnauthorizedAccessException("Collection is private");
        }

        CollectionReturnDto collectionReturn = modelMapper.map(collection, CollectionReturnDto.class);
        collectionReturn.setShows(showsInCollectionRepository.findByIdCollectionId(collectionId));

        // If the user is logged in, check if they have liked the collection
        if (loggedInUser != null) {
            collectionReturn.setLikedByUser(likedCollectionsRepository.existsByUserIdAndCollectionId(loggedInUser.getId(), collectionId));
        }
        return collectionReturn;
    }

    @Transactional
    public void likeCollection(Long collectionId) {
        User loggedInUser = authService.retrieveUserFromJwt();

        Optional<Collection> collectionOpt = collectionsRepository.findById(collectionId);
        if (collectionOpt.isEmpty()) {
            throw new CollectionNotFoundException("Collection not found with ID: " + collectionId);
        }
        Collection collection = collectionOpt.get();

        // Check if the user has already liked this collection
        if (likedCollectionsRepository.existsByUserIdAndCollectionId(loggedInUser.getId(), collectionId)) {
            throw new AlreadyLikedException("You have already liked this collection");
        }

        LikedCollection newLike = new LikedCollection();
        newLike.setCollectionId(collectionId);
        newLike.setUserId(loggedInUser.getId());
        likedCollectionsRepository.save(newLike);
        collectionsRepository.incrementLikes(collectionId);

        // Add the like collection event to the activities table, except for liking own collection
        if (!collection.getUserId().equals(loggedInUser.getId())) {
            Activity collectionEvent  = new Activity();
            collectionEvent.setUserId(collection.getUserId());
            collectionEvent.setActivityType(ActivityType.LIKE_COLLECTION.getDbValue());
            collectionEvent.setExternalId(newLike.getId());
            activitiesRepository.save(collectionEvent);
        }
    }

    @Transactional
    public void unlikeCollection(Long collectionId) {
        User loggedInUser = authService.retrieveUserFromJwt();

        if (collectionsRepository.existsById(collectionId)) {
            // Check to ensure the user has liked the collection
            Optional<LikedCollection> likedCollectionsOpt = likedCollectionsRepository.findByUserIdAndCollectionId(loggedInUser.getId(), collectionId);
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

    public List<DynamicRankingReturnDto> getUserDynamicRankings(String username, Integer limit) {
        ensureUserExistsByUsername(username);
        return dynamicRankingRepository.findByUsername(username, getPageRequest(limit));
    }
}
