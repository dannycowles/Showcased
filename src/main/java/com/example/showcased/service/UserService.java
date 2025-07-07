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

import java.util.Arrays;
import java.util.List;
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
                       DynamicRankingRepository dynamicRankingRepository) {
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
            headerData.setFollowing(followersRepository.existsById(new FollowerId(loggedInUserId, userId)));
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

        Follower followEntry = new Follower(new FollowerId(userId, followId));
        followersRepository.save(followEntry);
    }

    public void unfollowUser(Long unfollowId, HttpSession session) {
        ensureUserExists(unfollowId);
        Long userId = (Long) session.getAttribute("user");
        followersRepository.deleteById(new FollowerId(userId, unfollowId));
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
        return followersRepository.countByIdFollowingId(userId);
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
        return followersRepository.countByIdFollowerId(userId);
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
        collectionReturn.setNumLikes(likedCollectionsRepository.countByIdCollectionId(collectionId));

        // If the user is logged in, check if they have liked the collection
        if (userId != null) {
            collectionReturn.setLikedByUser(likedCollectionsRepository.existsById(new LikedCollectionsId(userId, collectionId)));
        }
        return collectionReturn;
    }

    public void likeCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        LikedCollections newLike = new LikedCollections(new LikedCollectionsId(userId, collectionId)); ;

        // Check if the user has already liked this collection
        if (likedCollectionsRepository.existsById(newLike.getId())) {
            throw new AlreadyLikedException("You have already liked this collection");
        }
        likedCollectionsRepository.save(newLike);
    }

    public void unlikeCollection(Long collectionId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        LikedCollections removeLike = new LikedCollections(new LikedCollectionsId(userId, collectionId));

        // Check to make sure the user has actually liked this collection
        if (!likedCollectionsRepository.existsById(removeLike.getId())) {
            throw new HaventLikedException("You have not liked this collection");
        }
        likedCollectionsRepository.delete(removeLike);
    }

    public List<DynamicRankingReturnDto> getUserDynamicRankings(Long userId, Integer limit) {
        ensureUserExists(userId);
        return dynamicRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }
}
