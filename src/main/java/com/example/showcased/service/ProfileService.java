package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.exception.AlreadyOnListException;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final UserRepository userRepository;
    private final FollowersRepository followersRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final SeasonInfoRepository seasonInfoRepository;

    public ProfileService(WatchlistRepository watchlistRepository,
                          ShowInfoRepository showInfoRepository,
                          ModelMapper modelMapper,
                          WatchingRepository watchingRepository,
                          ShowRankingRepository showRankingRepository,
                          ReviewRepository reviewRepository,
                          EpisodeInfoRepository episodeInfoRepository,
                          EpisodeRankingRepository episodeRankingRepository,
                          UserRepository userRepository,
                          FollowersRepository followersRepository,
                          SeasonRankingRepository seasonRankingRepository,
                          SeasonInfoRepository seasonInfoRepository) {
        this.watchlistRepository = watchlistRepository;
        this.showInfoRepository = showInfoRepository;
        this.watchingRepository = watchingRepository;
        this.modelMapper = modelMapper;
        this.showRankingRepository = showRankingRepository;
        this.reviewRepository = reviewRepository;
        this.episodeInfoRepository = episodeInfoRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.userRepository = userRepository;
        this.followersRepository = followersRepository;
        this.seasonRankingRepository = seasonRankingRepository;
        this.seasonInfoRepository = seasonInfoRepository;
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

    public ProfileDetailsDto getProfileDetails(HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        ProfileDetailsDto profileDetails = new ProfileDetailsDto();
        profileDetails.setUsername(user.getUsername());
        profileDetails.setProfilePicture(user.getProfilePicture());
        profileDetails.setWatchlistTop(getWatchlistTop(session));
        profileDetails.setWatchingTop(getWatchingListTop(session));
        profileDetails.setShowRankingTop(getShowRankingListTop(session));
        profileDetails.setEpisodeRankingTop(getEpisodeRankingList(numTopEntries, session));
        profileDetails.setReviews(getReviews(session));
        profileDetails.setNumFollowers(getFollowersCount(session));
        profileDetails.setNumFollowing(getFollowingCount(session));
        profileDetails.setSeasonRankingTop(getSeasonRankingList(numTopEntries, session));
        return profileDetails;
    }

    public void addShowToWatchlist(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's watchlist, if so we throw an exception
        if (watchlistRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on watchlist");
        }
        watchlistRepository.save(modelMapper.map(show, Watchlist.class));
    }

    public List<WatchReturnDto> getWatchlist(HttpSession session) {
        return watchlistRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<WatchReturnDto> getWatchlistTop(HttpSession session) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchlistRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
    }

    public void removeFromWatchlist(String id, HttpSession session) {
        watchlistRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToWatchingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        // First we need to delete the show from the user's watchlist
        watchlistRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // Then, we add the show to the currently watching list
        Watching watching = new Watching();
        watching.setId(new WatchId(userId, Long.valueOf(id)));
        watchingRepository.save(watching);
    }




    public void addShowToWatchingList(WatchSendDto show, HttpSession session) {
        show.setUserId((Long) session.getAttribute("user"));
        addToShowInfoRepository(show);

        // Check if the show is already in the user's currently watching list, if so we throw an exception
        if (watchingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on watching list");
        }
        watchingRepository.save(modelMapper.map(show, Watching.class));
    }

    public List<WatchReturnDto> getWatchingList(HttpSession session) {
        return watchingRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<WatchReturnDto> getWatchingListTop(HttpSession session) {
        PageRequest pageRequest = PageRequest.of(0, numTopEntries);
        return watchingRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
    }

    public void removeFromWatchingList(String id, HttpSession session) {
        watchingRepository.deleteById(new WatchId((Long) session.getAttribute("user"), Long.valueOf(id)));
    }

    public void moveToRankingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        // First we need to delete the show from the user's currently watching list
        watchingRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // Then, we add the show to the user's ranking list
        // Check if the user's ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = showRankingRepository.findMaxRankNumByUserId(userId);
        ShowRanking ranking = new ShowRanking();
        ranking.setId(new WatchId(userId, Long.valueOf(id)));
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

        // Check if the show is already in the user's show ranking list, if so we throw an exception
        if (showRankingRepository.existsById(new WatchId(show.getUserId(), show.getShowId()))) {
            throw new AlreadyOnListException("Show is already on ranking list");
        }

        // Check if the user's show ranking list is empty, if so it's rank number will be 1,
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
        return showRankingRepository.findByUserIdTop((Long) session.getAttribute("user"), pageRequest);
    }

    public void removeFromShowRankingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        showRankingRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<RankingReturnDto> rankings = showRankingRepository.findByUserId(userId);
        for (int i = 0; i < rankings.size(); i++) {
            ShowRanking ranking = new ShowRanking();
            ranking.setId(new WatchId(userId, rankings.get(i).getShowId()));
            ranking.setRankNum(i + 1L);
            showRankingRepository.save(ranking);
        }
    }

    public void updateShowRankingList(List<UpdateShowRankingDto> shows, HttpSession session) {
        for (UpdateShowRankingDto show : shows) {
            ShowRanking newRanking = modelMapper.map(show, ShowRanking.class);
            newRanking.getId().setUserId((Long) session.getAttribute("user"));
            showRankingRepository.save(newRanking);
        }
    }




    public void addEpisodeToRankingList(EpisodeRankingDto episode, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        EpisodeRanking ranking = new EpisodeRanking();
        EpisodeRankingId rankingId = new EpisodeRankingId(userId, episode.getShowId(), episode.getSeason(), episode.getEpisode());
        ranking.setId(rankingId);

        // If the episode doesn't exist in the episode info table already we add it for easy access
        if (!episodeInfoRepository.existsById(new EpisodeInfoId(episode.getShowId(), episode.getSeason(), episode.getEpisode()))) {
            EpisodeInfo episodeInfo = new EpisodeInfo();
            episodeInfo.setId(new EpisodeInfoId(episode.getShowId(), episode.getSeason(), episode.getEpisode())); // Manually setting the composite key
            episodeInfo.setShowTitle(episode.getShowTitle());
            episodeInfo.setEpisodeTitle(episode.getEpisodeTitle());
            episodeInfo.setPosterPath(episode.getPosterPath());

            episodeInfoRepository.save(episodeInfo);
        }

        // Check if the show is already in the user's episode ranking list, if so we throw an exception
        if (episodeRankingRepository.existsById(rankingId)) {
            throw new AlreadyOnListException("Episode is already on ranking list");
        }

        // Check if the user's episode ranking list is empty, if so it's rank number will be 1,
        // else it wil be added to the end of the list
        Integer maxRank = episodeRankingRepository.findMaxRankNumByUserId(userId);
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum((long) (maxRank + 1));
        }
        episodeRankingRepository.save(ranking);
    }

    public List<EpisodeRankingReturnDto> getEpisodeRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // If a limit was provided, use that, else retrieve the entire ranking list
        Pageable pageRequest;
        if (limit != null) {
            pageRequest = PageRequest.of(0, limit);
        } else {
            pageRequest = Pageable.unpaged();
        }
        return episodeRankingRepository.findByIdUserId(userId, pageRequest);
    }

    public void removeFromEpisodeRankingList(Long showId, int seasonNumber, int episodeNumber, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        episodeRankingRepository.deleteById(new EpisodeRankingId(userId, showId, seasonNumber, episodeNumber));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<EpisodeRankingReturnDto> rankings = episodeRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            EpisodeRanking ranking = new EpisodeRanking();
            ranking.setId(new EpisodeRankingId(userId, rankings.get(i).getShowId(), rankings.get(i).getSeason(), rankings.get(i).getEpisode()));
            ranking.setRankNum(i + 1L);
            episodeRankingRepository.save(ranking);
        }
    }

    public void updateEpisodeRankingList(List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        for (UpdateEpisodeRankingDto episode : episodes) {
            EpisodeRanking newRanking = new EpisodeRanking();
            newRanking.setId(new EpisodeRankingId((Long) session.getAttribute("user"), episode.getShowId(), episode.getSeason(), episode.getEpisode()));
            newRanking.setRankNum(episode.getRankNum());
            episodeRankingRepository.save(newRanking);
        }
    }




    public void addSeasonToRankingList(SeasonRankingDto season, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        SeasonRanking ranking = new SeasonRanking();
        SeasonRankingId rankingId = new SeasonRankingId(userId, season.getId());
        ranking.setId(rankingId);

        // If season doesn't exist in season info table add it
        if (!seasonInfoRepository.existsById(season.getId())) {
            SeasonInfo seasonInfo = modelMapper.map(season, SeasonInfo.class);
            seasonInfoRepository.save(seasonInfo);
        }

        // If the season is already on the user's ranking list throw exception
        if (seasonRankingRepository.existsById(rankingId)) {
            throw new AlreadyOnListException("Season is already on ranking list");
        }

        // If user's ranking list is empty, rank will start at 1 else append to end
        Integer maxRank = seasonRankingRepository.findMaxRankNumByUserId(userId);
        if (maxRank == null) {
            ranking.setRankNum(1L);
        } else {
            ranking.setRankNum(maxRank + 1L);
        }
        seasonRankingRepository.save(ranking);
    }

    public List<SeasonRankingReturnDto> getSeasonRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // If a limit was provided, use that, else retrieve the entire ranking list
        Pageable pageRequest;
        if (limit != null) {
            pageRequest = PageRequest.of(0, limit);
        } else {
            pageRequest = Pageable.unpaged();
        }
        return seasonRankingRepository.findByIdUserId(userId, pageRequest);
    }

    public void removeFromSeasonRankingList(Long seasonId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        seasonRankingRepository.deleteById(new SeasonRankingId(userId, seasonId));

        // After deleting the season we need to adjust all the rank numbers to account for it
        List<SeasonRankingReturnDto> rankings = seasonRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            SeasonRanking newRanking = new SeasonRanking();
            newRanking.setId(new SeasonRankingId(userId, rankings.get(i).getId()));
            newRanking.setRankNum(i + 1L);
            seasonRankingRepository.save(newRanking);
        }
    }

    public void updateSeasonRankingList(List<UpdateSeasonRankingDto> seasons, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        seasons.forEach(season -> {
            SeasonRanking newRanking = modelMapper.map(season, SeasonRanking.class);
            newRanking.setId(new SeasonRankingId(userId, season.getId()));
            newRanking.setRankNum(season.getRankNum());
            seasonRankingRepository.save(newRanking);
        });
    }




    public List<ReviewWithUserInfoDto> getReviews(HttpSession session) {
        return reviewRepository.findByUserId((Long) session.getAttribute("user"));
    }

    public List<UserSearchDto> getFollowers(HttpSession session) {
        return followersRepository.getFollowersByIdFollowingId((Long) session.getAttribute("user"));
    }

    public Long getFollowersCount(HttpSession session) {
        return followersRepository.countByIdFollowingId((Long) session.getAttribute("user"));
    }

    public List<UserSearchDto> getFollowing(HttpSession session) {
        return followersRepository.getFollowingByIdFollowerId((Long) session.getAttribute("user"));
    }

    public Long getFollowingCount(HttpSession session) {
        return followersRepository.countByIdFollowerId((Long) session.getAttribute("user"));
    }
}
