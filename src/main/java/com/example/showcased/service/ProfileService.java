package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.exception.AlreadyOnListException;
import com.example.showcased.exception.InvalidCharacterType;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfileService {

    private final WatchlistRepository watchlistRepository;
    private final ShowInfoRepository showInfoRepository;
    private final WatchingRepository watchingRepository;
    private final ModelMapper modelMapper;
    private final ShowRankingRepository showRankingRepository;
    private final int numTopEntries = 10;
    private final String[] validCharacterTypes = {"Protagonist", "Deuteragonist", "Antagonist"};
    private final ReviewRepository reviewRepository;
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final UserRepository userRepository;
    private final FollowersRepository followersRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final SeasonInfoRepository seasonInfoRepository;
    private final CharacterRankingRepository characterRankingRepository;

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
                          SeasonInfoRepository seasonInfoRepository,
                          CharacterRankingRepository characterRankingRepository) {
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
        this.characterRankingRepository = characterRankingRepository;
    }

    /**
     * Whenever a user adds a show to any of their lists,
     * the show info table might need to be updated too
     */
    private void addToShowInfoRepository(WatchSendDto show) {
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
        profileDetails.setWatchlistTop(getWatchlist(numTopEntries, session));
        profileDetails.setWatchingTop(getWatchingList(numTopEntries, session));
        profileDetails.setShowRankingTop(getShowRankingList(numTopEntries, session));
        profileDetails.setEpisodeRankingTop(getEpisodeRankingList(numTopEntries, session));
        profileDetails.setReviews(getReviews(session));
        profileDetails.setNumFollowers(getFollowersCount(session));
        profileDetails.setNumFollowing(getFollowingCount(session));
        profileDetails.setSeasonRankingTop(getSeasonRankingList(numTopEntries, session));
        profileDetails.setProtagonistRankingTop(getCharacterRankingList(numTopEntries, validCharacterTypes[0], session));
        profileDetails.setDeuteragonistRankingTop(getCharacterRankingList(numTopEntries, validCharacterTypes[1], session));
        profileDetails.setAntagonistRankingTop(getCharacterRankingList(numTopEntries, validCharacterTypes[2], session));
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

    // If a limit was provided, use that, else retrieve the entire ranking list
    private Pageable getPageRequest(Integer limit) {
        if (limit != null) {
            return PageRequest.of(0, limit);
        } else {
            return Pageable.unpaged();
        }
    }

    public List<WatchReturnDto> getWatchlist(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return watchlistRepository.findByIdUserId(userId, getPageRequest(limit));
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

    public List<WatchReturnDto> getWatchingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return watchingRepository.findByIdUserId(userId, getPageRequest(limit));
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

    public List<RankingReturnDto> getShowRankingList(Integer limit, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromShowRankingList(String id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        showRankingRepository.deleteById(new WatchId(userId, Long.valueOf(id)));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<RankingReturnDto> rankings = showRankingRepository.findByIdUserId(userId, Pageable.unpaged());
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
        EpisodeRankingId rankingId = new EpisodeRankingId(userId, episode.getId());
        ranking.setId(rankingId);

        // If the episode doesn't exist in the episode info table already we add it for easy access
        if (!episodeInfoRepository.existsById(episode.getId())) {
            EpisodeInfo episodeInfo = new EpisodeInfo();
            episodeInfo.setId(episode.getId());
            episodeInfo.setShowId(episode.getShowId());
            episodeInfo.setShowTitle(episode.getShowTitle());
            episodeInfo.setEpisodeTitle(episode.getEpisodeTitle());
            episodeInfo.setPosterPath(episode.getPosterPath());
            episodeInfo.setSeason(episode.getSeason());
            episodeInfo.setEpisode(episode.getEpisode());

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
        return episodeRankingRepository.findByIdUserId(userId, getPageRequest(limit));
    }

    public void removeFromEpisodeRankingList(Long episodeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        episodeRankingRepository.deleteById(new EpisodeRankingId(userId, episodeId));

        // After deleting from the show ranking list we will need to adjust the ranking numbers to account for it
        List<EpisodeRankingReturnDto> rankings = episodeRankingRepository.findByIdUserId(userId, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            EpisodeRanking ranking = new EpisodeRanking();
            ranking.setId(new EpisodeRankingId(userId, rankings.get(i).getId()));
            ranking.setRankNum(i + 1L);
            episodeRankingRepository.save(ranking);
        }
    }

    public void updateEpisodeRankingList(List<UpdateEpisodeRankingDto> episodes, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        for (UpdateEpisodeRankingDto episode : episodes) {
            EpisodeRanking newRanking = new EpisodeRanking();
            newRanking.setId(new EpisodeRankingId(userId, episode.getEpisodeId()));
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
        return seasonRankingRepository.findByIdUserId(userId, getPageRequest(limit));
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





    public void addCharacterToRankingList(CharacterRankingDto character, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure the character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(character.getCharacterType())) {
            throw new InvalidCharacterType("Invalid character type: " + character.getCharacterType());
        }

        CharacterRanking ranking = modelMapper.map(character, CharacterRanking.class);
        CharacterRankingId rankingId = new CharacterRankingId(userId, character.getCharacterName());
        ranking.setId(rankingId);

        // Check if the character is already on the user's list
        if (characterRankingRepository.existsById(rankingId)) {
            throw new AlreadyOnListException("Character is already on ranking list");
        }

        // If user's ranking list for that type is empty, start at 1 else append to end
        Integer maxRank = characterRankingRepository.findMaxRankNumByCharacterType(userId, character.getCharacterType());
        if (maxRank != null) {
            ranking.setRankNum(maxRank + 1);
        } else {
            ranking.setRankNum(1);
        }
        characterRankingRepository.save(ranking);
    }

    public List<CharacterRankingReturnDto> getCharacterRankingList(Integer limit, String characterType, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, getPageRequest(limit));
    }

    public void removeFromCharacterRankingList(String characterType, String name, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        characterRankingRepository.deleteById(new CharacterRankingId(userId, name));

        // After deleting the character, we need to update the ranks of the other characters on list
        List<CharacterRankingReturnDto> rankings = characterRankingRepository.findByIdUserIdAndCharacterType(userId, characterType, Pageable.unpaged());
        for (int i = 0; i < rankings.size(); i++) {
            CharacterRanking newRanking = new CharacterRanking();
            newRanking.setId(new CharacterRankingId(userId, rankings.get(i).getCharacterName()));
            newRanking.setRankNum(i + 1);
            newRanking.setCharacterType(characterType);
            newRanking.setShowName(rankings.get(i).getShowName());
            characterRankingRepository.save(newRanking);
        }
    }


    public void updateCharacterRankingList(UpdateCharacterRankingsDto updates, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check to ensure character type is valid
        if (!Arrays.asList(validCharacterTypes).contains(updates.getCharacterType())) {
            throw new InvalidCharacterType("Invalid character type: " + updates.getCharacterType());
        }

        // Update rankings
        updates.getUpdates().forEach( update -> {
            CharacterRanking newRanking = new CharacterRanking();
            newRanking.setId(new CharacterRankingId(userId, update.getCharacterName()));
            newRanking.setRankNum(update.getRankNum());
            newRanking.setShowName(update.getShowName());
            newRanking.setCharacterType(updates.getCharacterType());
            characterRankingRepository.save(newRanking);
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

    public void removeFollower(Long removeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        followersRepository.deleteById(new FollowerId(removeId, userId));
    }
}
