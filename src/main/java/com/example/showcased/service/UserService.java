package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.User;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public UserService(ShowRankingRepository showRankingRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       UserRepository userRepository,
                       ModelMapper modelMapper, ReviewRepository reviewRepository) {
        this.showRankingRepository = showRankingRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
    }

    public List<UserSearchDto> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query).stream()
                .map(user -> modelMapper.map(user, UserSearchDto.class))
                .collect(Collectors.toList());
    }

    public ProfileDetailsDto getUserDetails(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        ProfileDetailsDto userDetails = new ProfileDetailsDto();

        // If the user was not found we throw an exception
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        } else {
            userDetails.setUsername(user.get().getUsername());
            userDetails.setWatchlistTop(getUserWatchlistTop(userId));
            userDetails.setWatchingTop(getUserWatchingListTop(userId));
            userDetails.setShowRankingTop(getUserShowRankingsTop(userId));
            userDetails.setEpisodeRankingTop(getUserEpisodeRankingsTop(userId));
            userDetails.setReviews(getUserReviews(userId));
        }
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
}
