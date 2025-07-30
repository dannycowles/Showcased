package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.enums.ActivityType;
import com.example.showcased.exception.AlreadyLikedException;
import com.example.showcased.exception.HaventLikedException;
import com.example.showcased.exception.ItemNotFoundException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShowService {

    private final ModelMapper modelMapper;
    private final ShowInfoRepository showInfoRepository;
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final ActivitiesRepository activitiesRepository;
    @Value("${omdbApi}")
    private String omdbKey;

    private final ShowReviewRepository showReviewRepository;
    private final LikedShowReviewsRepository likedShowReviewsRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchingRepository watchingRepository;
    private final ShowRankingRepository showRankingRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final EpisodeReviewRepository episodeReviewRepository;
    private final LikedEpisodeReviewsRepository likedEpisodeReviewsRepository;
    private final TMDBClient tmdbClient;
    private final OMDBClient omdbClient;
    private final ShowReviewCommentRepository showReviewCommentRepository;
    private final LikedShowReviewCommentsRepository  likedShowReviewCommentsRepository;
    private final EpisodeReviewCommentRepository episodeReviewCommentRepository;
    private final LikedEpisodeReviewCommentsRepository likedEpisodeReviewCommentsRepository;
    private final int numComments = 2;

    public ShowService(ShowReviewRepository showReviewRepository,
                       ModelMapper modelMapper,
                       LikedShowReviewsRepository likedShowReviewsRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       ShowRankingRepository showRankingRepository,
                       SeasonRankingRepository seasonRankingRepository,
                       TMDBClient tmdbClient,
                       OMDBClient omdbClient,
                       ShowInfoRepository showInfoRepository,
                       EpisodeReviewRepository episodeReviewRepository,
                       LikedEpisodeReviewsRepository likedEpisodeReviewsRepository,
                       EpisodeInfoRepository episodeInfoRepository,
                       EpisodeRankingRepository episodeRankingRepository,
                       ShowReviewCommentRepository showReviewCommentRepository,
                       LikedShowReviewCommentsRepository likedShowReviewCommentsRepository,
                       EpisodeReviewCommentRepository episodeReviewCommentRepository,
                       LikedEpisodeReviewCommentsRepository likedEpisodeReviewCommentsRepository, ActivitiesRepository activitiesRepository) {
        this.showReviewRepository = showReviewRepository;
        this.modelMapper = modelMapper;
        this.likedShowReviewsRepository = likedShowReviewsRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.showRankingRepository = showRankingRepository;
        this.seasonRankingRepository = seasonRankingRepository;
        this.tmdbClient = tmdbClient;
        this.omdbClient = omdbClient;
        this.showInfoRepository = showInfoRepository;
        this.episodeReviewRepository = episodeReviewRepository;
        this.likedEpisodeReviewsRepository = likedEpisodeReviewsRepository;
        this.episodeInfoRepository = episodeInfoRepository;
        this.episodeRankingRepository = episodeRankingRepository;
        this.showReviewCommentRepository = showReviewCommentRepository;
        this.likedShowReviewCommentsRepository = likedShowReviewCommentsRepository;
        this.episodeReviewCommentRepository = episodeReviewCommentRepository;
        this.likedEpisodeReviewCommentsRepository = likedEpisodeReviewCommentsRepository;
        this.activitiesRepository = activitiesRepository;
    }

    // For each of the shows, retrieve the end year
    private void retrieveEndYears(ShowResultsPageDto shows) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (SearchDto searchResult : shows.getResults()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // Make request to TMDB show details endpoint
                String url = UriComponentsBuilder
                        .fromUriString("https://api.themoviedb.org/3/tv/")
                        .path(searchResult.getId())
                        .toUriString();
                JSONObject jsonResponse = new JSONObject(tmdbClient.getRaw(url));

                // Check if the show is in production, if so leave the last_air_date blank
                String endYear = "";
                if (!jsonResponse.optBoolean("in_production")) {
                    endYear = jsonResponse.optString("last_air_date").split("-")[0];
                }
                searchResult.setEndYear(endYear);
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    public ShowResultsPageDto searchByTitle(String query, Integer page) {
       // Make request to TMDB search endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/search/tv")
                .queryParam("query", query)
                .queryParam("page", page != null ? page : 1)
                .toUriString();

        ShowResultsPageDto shows = tmdbClient.get(url, ShowResultsPageDto.class);
        retrieveEndYears(shows);
        return shows;
    }

    public ShowResultsPageDto searchByGenre(Integer genre, Integer page) {
        // Make request to TMDB discover endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/discover/tv")
                .queryParam("with_genres", genre)
                .queryParam("page", page != null ? page : 1)
                .queryParam("sort_by", "vote_count.desc")
                .toUriString();

        ShowResultsPageDto shows = tmdbClient.get(url, ShowResultsPageDto.class);
        retrieveEndYears(shows);
        return shows;
    }

    public ShowDto getShowDetails(String id, HttpSession session) {
        // Make request to TMDB show details endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv/")
                .path(id)
                .toUriString();
        ShowDto show = tmdbClient.get(url, ShowDto.class);

        // Make request to TMDB external ID's endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "external_ids")
                .toUriString();
        JSONObject jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        String imdbId = jsonResponse.optString("imdb_id");
        show.setImdbId(imdbId);

        // Make request to TMDB cast endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "credits")
                .toUriString();
        CastResponseDto cast = tmdbClient.get(url, CastResponseDto.class);

        // Store only the first 5 stars (can modify later)
        show.setCast(cast.getCast().stream().limit(5).collect(Collectors.toList()));

        // Make request to OMDB show endpoint using the IMDB id
        url = UriComponentsBuilder
                .fromUriString("https://www.omdbapi.com")
                .queryParam("apikey", omdbKey)
                .queryParam("i", imdbId)
                .toUriString();
        jsonResponse = new JSONObject(omdbClient.getRaw(url));

        // Parse the response and extract the following information:
        show.setPlot(jsonResponse.optString("Plot"));
        show.setRating(jsonResponse.optString("Rated"));
        show.setAverageRuntime(jsonResponse.optString("Runtime"));
        show.setImdbRating(jsonResponse.optString("imdbRating"));
        show.setImdbVotes(jsonResponse.optString("imdbVotes"));
        show.setAwards(jsonResponse.optString("Awards"));

        // Check if the user is logged in, if so, check if the show is on watchlist/watching/ranking
        Long userId = (Long) session.getAttribute("user");
        if (userId != null) {
            show.setOnWatchlist(watchlistRepository.existsById(new WatchId(userId, Long.parseLong(id))));
            show.setOnWatchingList(watchingRepository.existsById(new WatchId(userId, Long.parseLong(id))));
            show.setOnRankingList(showRankingRepository.existsById(new WatchId(userId, Long.parseLong(id))));
        }

        // Make request to TMDB TV recommendations endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "recommendations")
                .toUriString();
        ShowResultsPageDto search = tmdbClient.get(url, ShowResultsPageDto.class);
        retrieveEndYears(search);
        show.setRecommendations(search.getResults());

        // Make request to TMDB watch providers endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "watch", "providers")
                .toUriString();
        jsonResponse = new JSONObject(tmdbClient.getRaw(url));

        // Retrieve the US based results for simplicity
        JSONObject results = jsonResponse.optJSONObject("results");
        results = results.optJSONObject("US");
        if (results == null) {
            return show;
        }

        // Parse and set the streaming options for the shows
        JSONArray streaming = results.optJSONArray("flatrate");
        List<WatchOptionDto> streamingOptions = new ArrayList<>();
        if (streaming != null) {
            for (int i = 0; i < streaming.length(); i++) {
                JSONObject streamingOption = streaming.getJSONObject(i);
                streamingOptions.add(new WatchOptionDto(streamingOption.optString("provider_name"), streamingOption.optString("logo_path")));
            }
        }

        // Parse and set the buy options for the shows
        JSONArray buy = results.optJSONArray("buy");
        List<WatchOptionDto> buyOptions = new ArrayList<>();
        if (buy != null) {
            for (int i = 0; i < buy.length(); i++) {
                JSONObject buyOption = buy.optJSONObject(i);
                buyOptions.add(new WatchOptionDto(buyOption.optString("provider_name"), buyOption.optString("logo_path")));
            }
        }

        show.setStreamingOptions(streamingOptions);
        show.setBuyOptions(buyOptions);

        return show;
    }

    public List<RoleDto> getCharacters(String id, String name) {
        // Make request to TMDB aggregate credits endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "aggregate_credits")
                .toUriString();

        List<RoleDto> roles = tmdbClient.get(url, CastWrapperDto.class).getCast().stream()
                .flatMap(character -> character.getRoles().stream())
                .filter(character -> character.getEpisode_count() > 1)
                .toList();
        List<RoleDto> filteredRoles = roles;

        // If a name/query is provided fuzzy search on that
        if (name != null && !name.isEmpty()) {
            List<String> names = roles.stream()
                    .map(role -> role.getName())
                    .distinct()
                    .toList();
            List<ExtractedResult> results = FuzzySearch.extractAll(name, names, 70);
            filteredRoles = results.stream()
                    .map(res -> roles.get(res.getIndex()))
                    .toList();
        }

        return filteredRoles;
    }

    public NumSeasonsDto getNumberOfSeasons(String showId) {
        // Make request to TMDB show endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv/")
                .path(showId)
                .toUriString();
        return tmdbClient.get(url, NumSeasonsDto.class);
    }

    public SeasonDto getSeasonDetails(String seasonNumber, String showId, HttpSession session) {
        // Make a request to TMDB season details endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(showId, "season", seasonNumber)
                .toUriString();
        SeasonDto season = tmdbClient.get(url, SeasonDto.class);

        // Make request to TMDB external ID's endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(showId, "external_ids")
                .toUriString();
        JSONObject jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        String imdbId = jsonResponse.optString("imdb_id");

        // Make a request to OMDB endpoint to retrieve IMDB ratings
        url = UriComponentsBuilder
                .fromUriString("https://www.omdbapi.com")
                .queryParam("apikey", omdbKey)
                .queryParam("i", imdbId)
                .queryParam("Season", seasonNumber)
                .toUriString();
        jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        JSONArray episodes = jsonResponse.getJSONArray("Episodes");
        season.setShowTitle(jsonResponse.optString("Title"));

        // This check ensures that unaired episodes don't appear
        // on both OMDB and TMDB don't cause alignment and indexing issues
        for (int i = episodes.length() - 1; i >= 0; i--) {
            JSONObject episode = episodes.getJSONObject(i);
            if (episode.optString("Released").equals("N/A")) {
                episodes.remove(i);
            }
        }

        // For each episode, extract the imdb rating and update the season object accordingly
        for (int i = 0; i < episodes.length(); i++) {
            JSONObject episode = episodes.getJSONObject(i);
            String rating = episode.optString("imdbRating");
            season.setRating(episode.optInt("Episode"), rating);
        }

        // For each of the episodes, extract the plot from the OMDB episode endpoint
        // previous TMDB endpoint contained spoilers in overview
        List<SeasonEpisodeDto> seasonEpisodes = season.getEpisodes();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (SeasonEpisodeDto episode : seasonEpisodes) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // Make request to OMDB episode endpoint
                String url2 = UriComponentsBuilder
                        .fromUriString("https://www.omdbapi.com")
                        .queryParam("apikey", omdbKey)
                        .queryParam("i", imdbId)
                        .queryParam("Season", seasonNumber)
                        .queryParam("Episode", episode.getEpisodeNumber())
                        .toUriString();
                JSONObject response = new JSONObject(omdbClient.getRaw(url2));

                if (!response.optString("Plot").equals("N/A") && response.optBoolean("Plot")) {
                    episode.setPlot(response.optString("Plot"));
                }
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        season.setEpisodes(seasonEpisodes);

        // If the user is logged in, check whether season is on their ranking list
        Long userId = (Long) session.getAttribute("user");
        if (userId != null) {
            season.setOnRankingList(seasonRankingRepository.existsById(new SeasonRankingId(userId, season.getId())));;
        }
        return season;
    }

    public ShowEpisodesDto getSeasonEpisodes(String showId, String seasonNumber) {
        // Make a request to TMDB season details endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(showId, "season", seasonNumber)
                .toUriString();
        return tmdbClient.get(url, ShowEpisodesDto.class);
    }

    public SeasonPartialDto getSeasonPartialDetails(Long showId, int seasonNumber) {
        // Make a request to TMDB season details endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(String.valueOf(showId), "season", String.valueOf(seasonNumber))
                .toUriString();
        return tmdbClient.get(url, SeasonPartialDto.class);
    }

    public EpisodeDto getEpisodeDetails(String episodeNumber, String seasonNumber, String showId, HttpSession session) {
        // Make a request to TMDB episode endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(showId, "season", seasonNumber, "episode", episodeNumber)
                .toUriString();
        EpisodeDto episode = tmdbClient.get(url, EpisodeDto.class);

        // Make request to TMDB external ID's endpoint
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(showId, "external_ids")
                .toUriString();
        JSONObject jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        String imdbId = jsonResponse.optString("imdb_id");

        // Make request to OMDB episode endpoint
        url = UriComponentsBuilder
                .fromUriString("https://www.omdbapi.com")
                .queryParam("apikey", omdbKey)
                .queryParam("i", imdbId)
                .queryParam("Season", seasonNumber)
                .queryParam("Episode", episode.getEpisodeNumber())
                .toUriString();
        jsonResponse = new JSONObject(omdbClient.getRaw(url));
        episode.setImdbVotes(jsonResponse.optString("imdbVotes"));
        episode.setImdbRating(jsonResponse.optString("imdbRating"));

        // Make request to TMDB show endpoint to retrieve show title
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv/")
                .path(showId)
                .toUriString();
        jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        episode.setShowTitle(jsonResponse.optString("name"));

        if (!jsonResponse.optString("Plot").equals("N/A") && jsonResponse.optBoolean("Plot")) {
            episode.setPlot(jsonResponse.optString("Plot"));
        }

        // If the user is logged in check if it's on their ranking list
        Long userId = (Long) session.getAttribute("user");
        if (userId != null) {
            episode.setOnRankingList(episodeRankingRepository.existsById(new  EpisodeRankingId(userId, episode.getId())));
        }
        return episode;
    }

    @Transactional
    public ShowReviewWithUserInfoDto addReviewToShow(Long showId, ShowReviewDto reviewDto, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Delete existing review if it exists
        showReviewRepository.deleteByUserIdAndShowId(userId, showId);
        showReviewRepository.flush();

        // Check if the show exists in the show info table
        if (!showInfoRepository.existsById(showId)) {
            ShowInfo showInfo = new ShowInfo();
            showInfo.setShowId(showId);
            showInfo.setPosterPath(reviewDto.getPosterPath());
            showInfo.setTitle(reviewDto.getShowTitle());
            showInfoRepository.save(showInfo);
        }

        ShowReview review = modelMapper.map(reviewDto, ShowReview.class);
        review.setUserId(userId);
        review.setShowId(showId);
        showReviewRepository.save(review);
        return showReviewRepository.findByIdWithUserInfo(review.getId());
    }

    public Page<ShowReviewWithUserInfoDto> getShowReviews(Long showId, HttpSession session, Pageable page) {
        Long userId = (Long) session.getAttribute("user");

        // Subtract 1 from provided page to align with 0-indexed pages, and ensure non-negative pages are requested
        Pageable modifiedPage = PageRequest.of(
                Math.max(0, page.getPageNumber() - 1),
                page.getPageSize(),
                page.getSort()
        );
        return showReviewRepository.findAllByShowId(showId, userId, modifiedPage);
    }

    public ShowReviewWithUserInfoDto getShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showReviewRepository.findById(reviewId, userId);
    }

    @Transactional
    public void likeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<ShowReview> reviewOpt = showReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find a show review with ID: " + reviewId);
        }
        ShowReview review = reviewOpt.get();

        // Check if the user has already liked the review, if so we throw an exception
        if (likedShowReviewsRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new AlreadyLikedException("You have already liked this show review");
        }

        LikedShowReview likedReview = new LikedShowReview();
        likedReview.setUserId(userId);
        likedReview.setReviewId(reviewId);
        likedShowReviewsRepository.save(likedReview);
        showReviewRepository.incrementLikes(reviewId);

        // Add the like show review event to the activity table, except liking own review
        if (!review.getUserId().equals(userId)) {
            Activity likeEvent = new Activity();
            likeEvent.setUserId(review.getUserId());
            likeEvent.setActivityType(ActivityType.LIKE_SHOW_REVIEW.getDbValue());
            likeEvent.setExternalId(likedReview.getId());
            activitiesRepository.save(likeEvent);
        }
    }

    @Transactional
    public void unlikeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check if the review exists, and if so has the user liked it
        if (showReviewRepository.existsById(reviewId)) {

            Optional<LikedShowReview> likedReviewOpt = likedShowReviewsRepository.findByUserIdAndReviewId(userId, reviewId);
            if (likedReviewOpt.isPresent()) {
                LikedShowReview likedReview = likedReviewOpt.get();

                likedShowReviewsRepository.delete(likedReview);
                showReviewRepository.decrementLikes(reviewId);
                activitiesRepository.deleteByExternalIdAndActivityType(likedReview.getId(), ActivityType.LIKE_SHOW_REVIEW.getDbValue());
            } else {
                throw new HaventLikedException("You have not liked this show review");
            }
        } else {
            throw new ItemNotFoundException("Didn't find a show review with ID: " + reviewId);
        }
    }

    @Transactional
    public EpisodeReviewWithUserInfoDto addReviewToEpisode(Long episodeId, EpisodeReviewDto reviewDto, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Delete existing review if it exists
        episodeReviewRepository.deleteByUserIdAndEpisodeId(userId, episodeId);
        episodeReviewRepository.flush();

        // Check if the episode exists in the episode info table
        if (!episodeInfoRepository.existsById(episodeId)) {
            EpisodeInfo episodeInfo = modelMapper.map(reviewDto, EpisodeInfo.class);
            episodeInfo.setId(episodeId);
            episodeInfoRepository.save(episodeInfo);
        }

        EpisodeReview review = modelMapper.map(reviewDto, EpisodeReview.class);
        review.setId(null);
        review.setEpisodeId(episodeId);
        review.setUserId(userId);
        episodeReviewRepository.save(review);
        return episodeReviewRepository.findByIdWithUserInfo(review.getId());
    }

    public Page<EpisodeReviewWithUserInfoDto> getEpisodeReviews(Long episodeId, HttpSession session, Pageable page) {
        Long userId = (Long) session.getAttribute("user");

        // Subtract 1 from provided page to align with 0-indexed pages, and ensure non-negative pages are requested
        Pageable modifiedPage = PageRequest.of(
                Math.max(0, page.getPageNumber() - 1),
                page.getPageSize(),
                page.getSort()
        );
        return episodeReviewRepository.findAllByEpisodeId(episodeId, userId, modifiedPage);
    }

    public EpisodeReviewWithUserInfoDto getEpisodeReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return episodeReviewRepository.findById(reviewId, userId);
    }

    @Transactional
    public void likeEpisodeReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<EpisodeReview> reviewOpt = episodeReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find a episode review with ID: " + reviewId);
        }
        EpisodeReview review = reviewOpt.get();

        // Check if the user has already liked the review, if so we throw an exception
        if (likedEpisodeReviewsRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new AlreadyLikedException("You have already liked this episode review");
        }

        LikedEpisodeReview likedReview =  new LikedEpisodeReview();
        likedReview.setUserId(userId);
        likedReview.setReviewId(reviewId);
        likedEpisodeReviewsRepository.save(likedReview);
        episodeReviewRepository.incrementLikes(reviewId);

        // Add the like episode review event to the activities table, except liking own review
        if (!review.getUserId().equals(userId)) {
            Activity likeEvent = new Activity();
            likeEvent.setUserId(review.getUserId());
            likeEvent.setActivityType(ActivityType.LIKE_EPISODE_REVIEW.getDbValue());
            likeEvent.setExternalId(likedReview.getId());
            activitiesRepository.save(likeEvent);
        }
    }

    @Transactional
    public void unlikeEpisodeReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check if the review exists, and if so, ensure the user has liked it already
        if (episodeReviewRepository.existsById(reviewId)) {
            Optional<LikedEpisodeReview> likedReviewOpt = likedEpisodeReviewsRepository.findByUserIdAndReviewId(userId, reviewId);
            if (likedReviewOpt.isPresent()) {
                LikedEpisodeReview likedReview = likedReviewOpt.get();

                likedEpisodeReviewsRepository.delete(likedReview);
                episodeReviewRepository.decrementLikes(reviewId);
                activitiesRepository.deleteByExternalIdAndActivityType(likedReview.getId(), ActivityType.LIKE_EPISODE_REVIEW.getDbValue());
            } else {
                throw new HaventLikedException("You have not liked this episode review");
            }
        } else {
            throw new ItemNotFoundException("Didn't find an episode review with ID: " + reviewId);
        }
    }


    public ShowResultsPageDto getTrendingShows(Integer page) {
        // Make request to TMDB trending TV endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/trending/tv/day")
                .queryParam("page", page != null ? page : 1)
                .toUriString();

        ShowResultsPageDto shows = tmdbClient.get(url, ShowResultsPageDto.class);
        retrieveEndYears(shows);
        return shows;
    }

    public AllGenresDto getAllGenres() {
        // Make request to TMDB TV genres endpoint
        String url = "https://api.themoviedb.org/3/genre/tv/list";
        return tmdbClient.get(url, AllGenresDto.class);
    }

    public ShowResultsPageDto getTopRatedShows(Integer page) {
        // Make request to TMDB top-rated shows endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv/top_rated")
                .queryParam("page", page != null ? page : 1)
                .toUriString();
        ShowResultsPageDto shows = tmdbClient.get(url, ShowResultsPageDto.class);
        retrieveEndYears(shows);
        return shows;
    }

    @Transactional
    public ReviewCommentWithUserInfoDto addCommentToShowReview(Long reviewId, ReviewCommentDto reviewComment, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<ShowReview> reviewOpt = showReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an review with ID: " + reviewId);
        }
        ShowReview review = reviewOpt.get();

        ShowReviewComment newComment = new ShowReviewComment();
        newComment.setUserId(userId);
        newComment.setReviewId(reviewId);
        newComment.setCommentText(reviewComment.getCommentText());
        showReviewCommentRepository.save(newComment);
        showReviewRepository.incrementNumComments(reviewId);

        // Add the show review comment event to activities table, except commenting on own review
        if (!review.getUserId().equals(userId)) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(review.getUserId());
            commentEvent.setActivityType(ActivityType.COMMENT_SHOW_REVIEW.getDbValue());
            commentEvent.setExternalId(newComment.getId());
            activitiesRepository.save(commentEvent);
        }
        return showReviewCommentRepository.findByIdWithUserInfo(newComment.getId());
    }

    public Page<ReviewCommentWithUserInfoDto> getShowReviewComments(Long reviewId, int page, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showReviewCommentRepository.findAllByReviewId(reviewId, userId, PageRequest.of(page - 1, numComments));
    }

    public ReviewCommentWithUserInfoDto getShowReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showReviewCommentRepository.findById(commentId, userId);
    }

    @Transactional
    public void likeShowReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<ShowReviewComment> commentOpt = showReviewCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an show review comment with ID: " + commentId);
        }
        ShowReviewComment comment = commentOpt.get();

        // Check if the user has already liked the comment
        if (likedShowReviewCommentsRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new AlreadyLikedException("You have already liked this show review comment");
        }

        LikedShowReviewComment likedComment = new LikedShowReviewComment();
        likedComment.setUserId(userId);
        likedComment.setCommentId(commentId);
        likedShowReviewCommentsRepository.save(likedComment);
        showReviewCommentRepository.incrementNumLikes(commentId);

        // Add the like show review comment event to the activities table, except liking own comment
        if (!comment.getUserId().equals(userId)) {
            Activity likeCommentEvent = new Activity();
            likeCommentEvent.setUserId(comment.getUserId());
            likeCommentEvent.setActivityType(ActivityType.LIKE_SHOW_REVIEW_COMMENT.getDbValue());
            likeCommentEvent.setExternalId(likedComment.getId());
            activitiesRepository.save(likeCommentEvent);
        }
    }

    @Transactional
    public void unlikeShowReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        if (showReviewCommentRepository.existsById(commentId)) {
            // Check to ensure the user has liked the comment
            Optional<LikedShowReviewComment> likedCommentOpt = likedShowReviewCommentsRepository.findByUserIdAndCommentId(userId, commentId);
            if (likedCommentOpt.isPresent()) {
                LikedShowReviewComment likedComment = likedCommentOpt.get();

                likedShowReviewCommentsRepository.delete(likedComment);
                showReviewCommentRepository.decrementNumLikes(commentId);
                activitiesRepository.deleteByExternalIdAndActivityType(likedComment.getId(), ActivityType.LIKE_SHOW_REVIEW_COMMENT.getDbValue());
            } else {
                throw new HaventLikedException("You have not liked this show review comment");
            }
        } else {
            throw new ItemNotFoundException("Didn't find an show review comment with ID: " + commentId);
        }
    }

    @Transactional
    public ReviewCommentWithUserInfoDto addCommentToEpisodeReview(Long reviewId, ReviewCommentDto reviewComment, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<EpisodeReview> reviewOpt = episodeReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an episode review with ID: " + reviewId);
        }
        EpisodeReview review = reviewOpt.get();

        EpisodeReviewComment newComment = new EpisodeReviewComment();
        newComment.setUserId(userId);
        newComment.setReviewId(reviewId);
        newComment.setCommentText(reviewComment.getCommentText());
        episodeReviewCommentRepository.save(newComment);
        episodeReviewRepository.incrementNumComments(reviewId);

        // Add episode review comment event to activities table, except commenting on own review
        if (!review.getUserId().equals(userId)) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(review.getUserId());
            commentEvent.setActivityType(ActivityType.COMMENT_EPISODE_REVIEW.getDbValue());
            commentEvent.setExternalId(newComment.getId());
            activitiesRepository.save(commentEvent);
        }

        return episodeReviewCommentRepository.findByIdWithUserInfo(newComment.getId());
    }

    public Page<ReviewCommentWithUserInfoDto> getEpisodeReviewComments(Long reviewId, int page, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return episodeReviewCommentRepository.findAllByReviewId(reviewId, userId, PageRequest.of(page - 1, numComments));
    }

    public ReviewCommentWithUserInfoDto getEpisodeReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return episodeReviewCommentRepository.findById(commentId, userId);
    }

    @Transactional
    public void likeEpisodeReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Optional<EpisodeReviewComment> commentOpt = episodeReviewCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an episode review comment with ID: " + commentId);
        }
        EpisodeReviewComment comment = commentOpt.get();

        // Check if the user has already liked the comment
        if (likedEpisodeReviewCommentsRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new AlreadyLikedException("You have already liked this episode review comment");
        }

        LikedEpisodeReviewComment likedComment = new LikedEpisodeReviewComment();
        likedComment.setUserId(userId);
        likedComment.setCommentId(commentId);
        likedEpisodeReviewCommentsRepository.save(likedComment);
        episodeReviewCommentRepository.incrementNumLikes(commentId);

        // Add the episode review comment like event to the activities table, except for liking own comment
        if (!comment.getUserId().equals(userId)) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(comment.getUserId());
            commentEvent.setActivityType(ActivityType.LIKE_EPISODE_REVIEW_COMMENT.getDbValue());
            commentEvent.setExternalId(likedComment.getId());
            activitiesRepository.save(commentEvent);
        }
    }

    @Transactional
    public void unlikeEpisodeReviewComment(Long commentId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        if (episodeReviewCommentRepository.existsById(commentId)) {
            Optional<LikedEpisodeReviewComment> likedCommentOpt = likedEpisodeReviewCommentsRepository.findByUserIdAndCommentId(userId, commentId);
            // Check to ensure the user has liked the comment
            if (likedCommentOpt.isPresent()) {
                LikedEpisodeReviewComment likedComment = likedCommentOpt.get();

                likedEpisodeReviewCommentsRepository.delete(likedComment);
                episodeReviewCommentRepository.decrementNumLikes(commentId);
                activitiesRepository.deleteByExternalIdAndActivityType(likedComment.getId(), ActivityType.LIKE_EPISODE_REVIEW_COMMENT.getDbValue());
            } else {
                throw new HaventLikedException("You have not liked this episode review comment");
            }
        } else {
            throw new ItemNotFoundException("Didn't find an episode review comment with ID: " + commentId);
        }
    }
}
