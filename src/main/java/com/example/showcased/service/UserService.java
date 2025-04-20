package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.Follower;
import com.example.showcased.entity.FollowerId;
import com.example.showcased.entity.User;
import com.example.showcased.exception.FollowSelfException;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public UserService(ShowRankingRepository showRankingRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       UserRepository userRepository,
                       ModelMapper modelMapper,
                       ReviewRepository reviewRepository,
                       FollowersRepository followersRepository) {
        this.showRankingRepository = showRankingRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
        this.followersRepository = followersRepository;
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
        userDetails.setWatchlistTop(getUserWatchlistTop(userId));
        // If the user has more than num top entries in their watchlist toggle the flag
        if (watchlistRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreWatchlist(true);
        }

        userDetails.setWatchingTop(getUserWatchingListTop(userId));
        // If the user has more than num top entries in their watching list toggle the flag
        if (watchingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreWatching(true);
        }

        userDetails.setShowRankingTop(getUserShowRankingsTop(userId));
        // If the user has more than num top entries in their show ranking list toggle the flag
        if (showRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreShowRanking(true);
        }

        userDetails.setEpisodeRankingTop(getUserEpisodeRankingsTop(userId));
        // If the user has more than num top entries in their episode ranking list toggle the flag
        if (episodeRankingRepository.countByIdUserId(userId) > numTopEntries) {
            userDetails.setMoreEpisodeRanking(true);
        }

        userDetails.setReviews(getUserReviews(userId));
        userDetails.setNumFollowers(getFollowersCount(userId));
        userDetails.setNumFollowing(getFollowingCount(userId));

        // Check if the user is logged in, if so we check they are following this user
        Long loggedInUserId = (Long) session.getAttribute("user");
        if (loggedInUserId != null) {
            userDetails.setFollowing(followersRepository.existsById(new FollowerId(loggedInUserId, userId)));
            userDetails.setOwnProfile(loggedInUserId.equals(userId));
        }
        return userDetails;
    }


    public List<WatchReturnDto> getUserWatchlist(Long userId) {
        ensureUserExists(userId);
        return watchlistRepository.findByUserId(userId);
    }

    public List<WatchReturnDto> getUserWatchlistTop(Long userId) {
        ensureUserExists(userId);
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchlistRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<WatchReturnDto> getUserWatchingList(Long userId) {
        ensureUserExists(userId);
        return watchingRepository.findByUserId(userId);
    }

    public List<WatchReturnDto> getUserWatchingListTop(Long userId) {
        ensureUserExists(userId);
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchingRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<RankingReturnDto> getUserShowRankings(Long userId) {
        ensureUserExists(userId);
        return showRankingRepository.findByUserId(userId);
    }

    public List<RankingReturnDto> getUserShowRankingsTop(Long userId) {
        ensureUserExists(userId);
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return showRankingRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<EpisodeRankingReturnDto> getUserEpisodeRankings(Long userId) {
        ensureUserExists(userId);
        return episodeRankingRepository.findByUserId(userId);
    }

    public List<EpisodeRankingReturnDto> getUserEpisodeRankingsTop(Long userId) {
        ensureUserExists(userId);
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return episodeRankingRepository.findByUserIdTop(userId, pageRequest);
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

    public void removeFollower(Long removeId, HttpSession session) {
        ensureUserExists(removeId);
        Long userId = (Long) session.getAttribute("user");
        followersRepository.deleteById(new FollowerId(removeId, userId));
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
}
