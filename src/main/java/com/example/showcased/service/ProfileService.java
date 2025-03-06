package com.example.showcased.service;

import com.example.showcased.dto.WatchlistReturnDto;
import com.example.showcased.dto.WatchlistSendDto;
import com.example.showcased.entity.ShowInfo;
import com.example.showcased.entity.Watchlist;
import com.example.showcased.entity.WatchlistId;
import com.example.showcased.exception.AlreadyOnListException;
import com.example.showcased.exception.NotLoggedInException;
import com.example.showcased.repository.ShowInfoRepository;
import com.example.showcased.repository.WatchlistRepository;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final ModelMapper modelMapper;

    public ProfileService(WatchlistRepository watchlistRepository, ShowInfoRepository showInfoRepository, ModelMapper modelMapper) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.modelMapper = modelMapper;
    }

    public void addShowToWatchlist(WatchlistSendDto show, HttpSession session) {
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
        if (watchlistRepository.existsById(new WatchlistId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException();
        }
        watchlistRepository.save(modelMapper.map(show, Watchlist.class));
    }

    public List<WatchlistReturnDto> getWatchlist(HttpSession session) {
        // If the user is not logged in they shouldn't be able to add to watchlist so we throw exception
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }
        return watchlistRepository.findByUserId((Long) session.getAttribute("user"));
    }
}
