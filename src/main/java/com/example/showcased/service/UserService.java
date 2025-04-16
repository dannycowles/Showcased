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

    public List<UserSearchDto> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query).stream()
                .map(user -> modelMapper.map(user, UserSearchDto.class))
                .collect(Collectors.toList());
    }

    public ProfileDetailsDto getUserDetails(Long userId) {
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
        return userDetails;
    }


    public List<WatchReturnDto> getUserWatchlist(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    public List<WatchReturnDto> getUserWatchlistTop(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchlistRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<WatchReturnDto> getUserWatchingList(Long userId) {
        return watchingRepository.findByUserId(userId);
    }

    public List<WatchReturnDto> getUserWatchingListTop(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchingRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<RankingReturnDto> getUserShowRankings(Long userId) {
        return showRankingRepository.findByUserId(userId);
    }

    public List<RankingReturnDto> getUserShowRankingsTop(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return showRankingRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<EpisodeRankingReturnDto> getUserEpisodeRankings(Long userId) {
        return episodeRankingRepository.findByUserId(userId);
    }

    public List<EpisodeRankingReturnDto> getUserEpisodeRankingsTop(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return episodeRankingRepository.findByUserIdTop(userId, pageRequest);
    }




    public List<ReviewWithUserInfoDto> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public void followUser(Long followId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Preventive code for if user tries to follow themselves
        if (userId.equals(followId)) {
            throw new FollowSelfException("A user cannot follow themselves.");
        }

        Follower followEntry = new Follower(new FollowerId(userId, followId));
        followersRepository.save(followEntry);
    }

    public void unfollowUser(Long unfollowId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        followersRepository.deleteById(new FollowerId(userId, unfollowId));
    }

    public List<UserSearchDto> getFollowers(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return followersRepository.getFollowersByIdFollowingId(userId);
    }

    public Long getFollowersCount(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return followersRepository.countByIdFollowingId(userId);
    }

    public List<UserSearchDto> getFollowing(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return followersRepository.getFollowingByIdFollowerId(userId);
    }

    public Long getFollowingCount(Long userId) {
        // Check to make sure user exists in system
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return followersRepository.countByIdFollowerId(userId);
    }
}
