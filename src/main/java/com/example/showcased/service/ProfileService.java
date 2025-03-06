package com.example.showcased.service;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.dto.WatchSendDto;
import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.ShowInfo;
import com.example.showcased.entity.Watching;
import com.example.showcased.entity.Watchlist;
import com.example.showcased.entity.WatchId;
import com.example.showcased.exception.AlreadyOnListException;
import com.example.showcased.exception.NotLoggedInException;
import com.example.showcased.repository.ShowInfoRepository;
import com.example.showcased.repository.WatchingRepository;
import com.example.showcased.repository.WatchlistRepository;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final WatchingRepository watchingRepository;
    private final ModelMapper modelMapper;

    public ProfileService(WatchlistRepository watchlistRepository,
                          ShowInfoRepository showInfoRepository,
                          ModelMapper modelMapper,
                          WatchingRepository watchingRepository) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.watchingRepository = watchingRepository;
        this.modelMapper = modelMapper;
    }

    public void addShowToWatchlist(WatchSendDto show, HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        show.setUserId((Long) session.getAttribute("user"));

        // Check if the show already exists in the show info table, if not add it
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }

        // Check if the show is already in the user's watchlist, if so we throw an exception
        if (watchlistRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }
        watchlistRepository.save(modelMapper.map(show, Watchlist.class));
    }

    public List<WatchReturnDto> getWatchlist(HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        return watchlistRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public void addShowToWatchingList(WatchSendDto show, HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        show.setUserId((Long) session.getAttribute("user"));

        // Check if the show already exists in the show info table, if not add it
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        if (watchingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }
        watchingRepository.save(modelMapper.map(show, Watching.class));
    }

    public List<WatchReturnDto> getWatchingList(HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        return watchingRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public void addShowToRankingList(WatchSendDto show, HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        show.setUserId((Long) session.getAttribute("user"));

        // Check if the show already exists in the show info table, if not add it
        if (!showInfoRepository.existsById(show.getShowId())) {
            ShowInfo showInfo = modelMapper.map(show, ShowInfo.class);
            showInfoRepository.save(showInfo);
        }

        // Get max of ranking list for the user, if no entries the rank should default to 1,
        // else sequentially increment (add to end of list)

    }

    public List<RankingReturnDto> getShowRankingList(HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }

        return null;
    }

}
