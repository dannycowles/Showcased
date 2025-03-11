package com.example.showcased.service;

import com.example.showcased.dto.EpisodeRankingReturnDto;
import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.repository.EpisodeRankingRepository;
import com.example.showcased.repository.ShowRankingRepository;
import com.example.showcased.repository.WatchingRepository;
import com.example.showcased.repository.WatchlistRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final ShowRankingRepository showRankingRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchingRepository watchingRepository;
    private final int numTopEntries = 10;

    public UserService(ShowRankingRepository showRankingRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository) {
        this.showRankingRepository = showRankingRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
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
}
