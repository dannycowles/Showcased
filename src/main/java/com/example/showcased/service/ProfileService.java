package com.example.showcased.service;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.dto.ReviewWithUserInfoDto;
import com.example.showcased.dto.WatchSendDto;
import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.*;
import com.example.showcased.exception.AlreadyOnListException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final WatchingRepository watchingRepository;
    private final ModelMapper modelMapper;
    private final ShowRankingRepository showRankingRepository;
    private final int numTopEntries = 10;
    private final ReviewRepository reviewRepository;

    public ProfileService(WatchlistRepository watchlistRepository,
                          ShowInfoRepository showInfoRepository,
                          ModelMapper modelMapper,
                          WatchingRepository watchingRepository, ShowRankingRepository showRankingRepository, ReviewRepository reviewRepository) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.watchingRepository = watchingRepository;
        this.modelMapper = modelMapper;
        this.showRankingRepository = showRankingRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Whenever a user adds a show to any of their lists,
     * the show info table might need to be updated too
     */
    public void addToShowInfoRepository(WatchSendDto show) {
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }
    }

    public void addShowToWatchlist(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's watchlist, if so we throw an exception
        if (watchlistRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }
        watchlistRepository.save(modelMapper.map(show, Watchlist.class));
    }

    public List<WatchReturnDto> getWatchlist(HttpSession session) {
        return watchlistRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<WatchReturnDto> getWatchlistTop(HttpSession session) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        Page<WatchReturnDto> page = watchlistRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
        return page.getContent();
    }

    public void removeFromWatchlist(String id, HttpSession session) {
        watchlistRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToWatchingList(String id, HttpSession session) {
        // First we need to delete the show from the user's watchlist
        watchlistRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));

        // Then, we add the show to the currently watching list
        Watching watching = new Watching();
        watching.setId(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
        watchingRepository.save(watching);
    }




    public void addShowToWatchingList(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        if (watchingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }
        watchingRepository.save(modelMapper.map(show, Watching.class));
    }

    public List<WatchReturnDto> getWatchingList(HttpSession session) {
        return watchingRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<WatchReturnDto> getWatchingListTop(HttpSession session) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        Page<WatchReturnDto> page = watchingRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
        return page.getContent();
    }

    public void removeFromWatchingList(String id, HttpSession session) {
        watchingRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToRankingList(String id, HttpSession session) {
        // First we need to delete the show from the user's currently watching list
        watchingRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));

        // Then, we add the show to the user's ranking list
        // Check if the user's ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId((Long) session.getAttribute("user"));
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
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

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        if (showRankingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }

        // Check if the user's ranking list is empty, if so it's rank number will be 1,
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

    public List<RankingReturnDto> getShowRankingList(HttpSession session) {
        return showRankingRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<RankingReturnDto> getShowRankingListTop(HttpSession session) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        Page<RankingReturnDto> page = showRankingRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
        return page.getContent();
    }

    public void removeFromShowRankingList(String id, HttpSession session) {
        showRankingRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));

        // After deleting from the ranking list we will need to adjust the ranking numbers to account for it
        List<RankingReturnDto> rankings = showRankingRepository.findByUserId((Long) session.getAttribute("user"));
        for (int i = 0; i < rankings.size(); i++) {
            ShowRanking ranking = new ShowRanking();
            ranking.setId(new WatchId((Long) session.getAttribute("user"), rankings.get(i).getShowId()));
            ranking.setRankNum(i + 1L);
            showRankingRepository.save(ranking);
        }
    }




    public List<ReviewWithUserInfoDto> getReviews(HttpSession session) {
        return reviewRepository.findByUserId((Long) session.getAttribute("user"));
    }
}
