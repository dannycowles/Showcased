package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.Collection;
import com.example.showcased.entity.Follower;
import com.example.showcased.entity.FollowerId;
import com.example.showcased.entity.User;
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
    private final ReviewRepository reviewRepository;
    private final FollowersRepository followersRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final CharacterRankingRepository characterRankingRepository;
    private final String[] validCharacterTypes = {"protagonist", "deuteragonist", "antagonist"};
    private final CollectionRepository collectionsRepository;
    private final ShowsInCollectionRepository showsInCollectionRepository;

    public UserService(ShowRankingRepository showRankingRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       UserRepository userRepository,
                       ModelMapper modelMapper,
                       ReviewRepository reviewRepository,
                       FollowersRepository followersRepository,
                       SeasonRankingRepository seasonRankingRepository,
                       CharacterRankingRepository characterRankingRepository,
                       CollectionRepository collectionsRepository,
                       ShowsInCollectionRepository showsInCollectionRepository) {
        this.showRankingRepository = showRankingRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
        this.followersRepository = followersRepository;
        this.seasonRankingRepository = seasonRankingRepository;
        this.characterRankingRepository = characterRankingRepository;
        this.collectionsRepository = collectionsRepository;
        this.showsInCollectionRepository = showsInCollectionRepository;
    }

    public void ensureUserExists(Long userId) {
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
        ProfileDetailsDto userDetails = new ProfileDetailsDto();

        // Throw exception if the user was not found by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userDetails.setUsername(user.getUsername());
        userDetails.setProfilePicture(user.getProfilePicture());
        userDetails.setWatchlistTop(getUserWatchlist(userId, numTopEntries));
        if (watchlistRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreWatchlist(true);
        }

        userDetails.setWatchingTop(getUserWatchingList(userId, numTopEntries));
        if (watchingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreWatching(true);
        }

        userDetails.setShowRankingTop(getUserShowRankings(userId, numTopEntries));
        if (showRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreShowRanking(true);
        }

        userDetails.setEpisodeRankingTop(getUserEpisodeRankings(userId, numTopEntries));
        if (episodeRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreEpisodeRanking(true);
        }

        userDetails.setSeasonRankingTop(getUserSeasonRankings(userId, numTopEntries));
        if (seasonRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreSeasonRanking(true);
        }

        userDetails.setReviews(getUserReviews(userId));
        userDetails.setNumFollowers(getFollowersCount(userId));
        userDetails.setNumFollowing(getFollowingCount(userId));
        userDetails.setCharacterRankings(getAllUserCharacterRankings(userId, numTopEntries));

        // Check if the user is logged in, if so we check they are following this user
        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            userDetails.setFollowing(followersRepository.existsById(new FollowerId(loggedInUserId, userId)));
            userDetails.setOwnProfile(loggedInUserId.equals(userId));
        }
        return userDetails;
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
        return rankings;
    }




    public List<ReviewWithUserInfoDto> getUserReviews(Long userId) {
        ensureUserExists(userId);
        return reviewRepository.findByUserId(userId);
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

    public List<UserSearchDto> getFollowers(Long userId, HttpSession session) {
        ensureUserExists(userId);
        List<UserSearchDto> followers = followersRepository.getFollowersByIdFollowingId(userId);

        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            Set<Long> followedUsers = followersRepository.getFollowingIds(loggedInUserId);

            // Iterate through followers list and check if set contains that id
            followers.forEach(user -> {
               user.setFollowing(followedUsers.contains(user.getId()));
               user.setOwnProfile(loggedInUserId.equals(user.getId()));
            });
        }

        return followers;
    }

    public Long getFollowersCount(Long userId) {
        ensureUserExists(userId);
        return followersRepository.countByIdFollowingId(userId);
    }

    public List<UserSearchDto> getFollowing(Long userId, HttpSession session) {
        ensureUserExists(userId);
        List<UserSearchDto> following = followersRepository.getFollowingByIdFollowerId(userId);

        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            Set<Long> followedUsers = followersRepository.getFollowingIds(loggedInUserId);

            // Iterate through followers list and check if set contains that id
            following.forEach(user -> {
                user.setFollowing(followedUsers.contains(user.getId()));
                user.setOwnProfile(loggedInUserId.equals(user.getId()));
            });
        }

        return following;
    }

    public Long getFollowingCount(Long userId) {
        ensureUserExists(userId);
        return followersRepository.countByIdFollowerId(userId);
    }




    public List<CollectionDto> getCollections(Long userId) {
        ensureUserExists(userId);
        return collectionsRepository.findByUserIdPublic(userId).stream()
                .map(collection -> modelMapper.map(collection, CollectionDto.class))
                .collect(Collectors.toList());
    }

    public CollectionReturnDto getShowsInCollection(Long userId, Long collectionId) {
        ensureUserExists(userId);
        Collection collection = collectionsRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection not found with ID: " + collectionId));

        if (collection.isPrivate()) {
            throw new UnauthorizedCollectionAccessException("Collection is private");
        }
        return new CollectionReturnDto(collection.getCollectionName(), false, showsInCollectionRepository.findByIdCollectionId(collectionId));
    }
}
