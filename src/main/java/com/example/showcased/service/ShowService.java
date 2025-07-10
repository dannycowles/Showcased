package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.exception.AlreadyLikedException;
import com.example.showcased.exception.HaventLikedException;
import com.example.showcased.exception.ItemNotFoundException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ModelMapper modelMapper;
    private final ShowInfoRepository showInfoRepository;
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
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
                       LikedEpisodeReviewsRepository likedEpisodeReviewsRepository, EpisodeInfoRepository episodeInfoRepository, EpisodeRankingRepository episodeRankingRepository) {
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

    public List<ShowReviewWithUserInfoDto> getShowReviews(Long showId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return showReviewRepository.findAllByShowId(showId, userId);
    }

    @Transactional
    public void likeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (!showReviewRepository.existsById(reviewId)) {
            throw new ItemNotFoundException("Didn't find a show review with ID: " + reviewId);
        }

        // Check if the user has already liked the review, if so we throw an exception
        LikedShowReviews likedReview = new LikedShowReviews(new LikedShowReviewsId(userId, reviewId));
        if (likedShowReviewsRepository.existsById(likedReview.getId())) {
            throw new AlreadyLikedException("You have already liked this show review");
        }

        likedShowReviewsRepository.save(likedReview);
        showReviewRepository.incrementLikes(reviewId);
    }

    @Transactional
    public void unlikeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (!showReviewRepository.existsById(reviewId)) {
            throw new ItemNotFoundException("Didn't find a show review with ID: " + reviewId);
        }

        // Check if the user has liked the review, if not we throw an exception
        LikedShowReviews likedReview = new LikedShowReviews(new LikedShowReviewsId(userId, reviewId));
        if (!likedShowReviewsRepository.existsById(likedReview.getId())) {
            throw new HaventLikedException("You have not liked this show review");
        }

        likedShowReviewsRepository.delete(likedReview);
        showReviewRepository.decrementLikes(reviewId);
    }

    @Transactional
    public void addReviewToEpisode(Long episodeId, EpisodeReviewDto reviewDto, HttpSession session) {
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
    }

    public List<EpisodeReviewWithUserInfoDto> getEpisodeReviews(Long episodeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        return episodeReviewRepository.findAllByEpisodeId(episodeId, userId);
    }

    @Transactional
    public void likeEpisodeReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (!episodeReviewRepository.existsById(reviewId)) {
            throw new ItemNotFoundException("Didn't find an episode review with ID: " + reviewId);
        }

        // Check if the user has already liked the review, if so we throw an exception
        LikedEpisodeReview likedReview = new LikedEpisodeReview(new LikedEpisodeReviewId(userId, reviewId));
        if (likedEpisodeReviewsRepository.existsById(likedReview.getId())) {
            throw new AlreadyLikedException("You have already liked this episode review");
        }

        likedEpisodeReviewsRepository.save(likedReview);
        episodeReviewRepository.incrementLikes(reviewId);
    }

    @Transactional
    public void unlikeEpisodeReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        if (!episodeReviewRepository.existsById(reviewId)) {
            throw new ItemNotFoundException("Didn't find an episode review with ID: " + reviewId);
        }

        // Check if the user hasn't liked the review yet
        LikedEpisodeReview likedReview = new LikedEpisodeReview(new LikedEpisodeReviewId(userId, reviewId));
        if (!likedEpisodeReviewsRepository.existsById(likedReview.getId())) {
            throw new HaventLikedException("You have not liked this episode review");
        }

        likedEpisodeReviewsRepository.delete(likedReview);
        episodeReviewRepository.decrementLikes(reviewId);
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
}
