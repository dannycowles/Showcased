package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.enums.ActivityType;
import com.example.showcased.exception.AlreadyLikedException;
import com.example.showcased.exception.HaventLikedException;
import com.example.showcased.exception.ItemNotFoundException;
import com.example.showcased.exception.UnauthorizedAccessException;
import com.example.showcased.repository.*;
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

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ModelMapper modelMapper;
    private final ShowInfoRepository showInfoRepository;
    private final EpisodeInfoRepository episodeInfoRepository;
    private final EpisodeRankingRepository episodeRankingRepository;
    private final ActivitiesRepository activitiesRepository;
    private final UserRepository userRepository;
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
    private final AuthService authService;
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
                       LikedEpisodeReviewCommentsRepository likedEpisodeReviewCommentsRepository,
                       ActivitiesRepository activitiesRepository,
                       AuthService authService, UserRepository userRepository) {
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
        this.authService = authService;
        this.userRepository = userRepository;
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

    public GenreShowResultsPageDto searchByGenre(int genre, Integer page) {
        // Make request to TMDB discover endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/discover/tv")
                .queryParam("with_genres", genre)
                .queryParam("page", page != null ? page : 1)
                .queryParam("sort_by", "vote_count.desc")
                .toUriString();

        GenreShowResultsPageDto shows = tmdbClient.get(url, GenreShowResultsPageDto.class);
        retrieveEndYears(shows);

        // Make request to TMDB genre endpoint to fetch the name of the genre
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/genre/tv/list")
                .toUriString();
        JSONObject jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        JSONArray genres = jsonResponse.getJSONArray("genres");

        for  (int i = 0; i < genres.length(); i++) {
            JSONObject genreObject = genres.getJSONObject(i);
            if (genreObject.getInt("id") == genre) {
                shows.setGenre(genreObject.getString("name"));
                break;
            }
        }
        return shows;
    }

    public ShowDto getShowDetails(String id) {
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
        User user = authService.retrieveUserFromJwt();
        if (user != null) {
            show.setOnWatchlist(watchlistRepository.existsById(new WatchId(user.getId(), Long.parseLong(id))));
            show.setOnWatchingList(watchingRepository.existsById(new WatchId(user.getId(), Long.parseLong(id))));
            show.setOnRankingList(showRankingRepository.existsById(new WatchId(user.getId(), Long.parseLong(id))));
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

        // Set show YouTube trailer if one exists
        url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/tv")
                .pathSegment(id, "videos")
                .toUriString();
        jsonResponse = new JSONObject(tmdbClient.getRaw(url));
        JSONArray videoResults = jsonResponse.optJSONArray("results");

        String trailerKey = null;
        Instant oldestTrailer = Instant.MAX;
        for (int i = 0; i < videoResults.length(); i++) {
            JSONObject video = videoResults.getJSONObject(i);
            String videoType =  video.optString("type");
            String videoSite = video.optString("site");

            if ("Trailer".equalsIgnoreCase(videoType) && "YouTube".equalsIgnoreCase(videoSite)) {
                String publishedAt = video.optString("published_at");
                Instant publishedInstant = Instant.parse(publishedAt);

                if (publishedInstant.isBefore(oldestTrailer)) {
                    oldestTrailer = publishedInstant;
                    trailerKey = video.optString("key");
                }
            }
        }

        if (trailerKey != null) {
            String trailerUrl = "https://www.youtube.com/embed/" + trailerKey;
            show.setTrailerPath(trailerUrl);
        }
        show.setReviewDistribution(showReviewRepository.getShowReviewDistribution(Long.valueOf(id)));
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
                    .map(RoleDto::getName)
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

    public SeasonDto getSeasonDetails(String seasonNumber, String showId) {
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
        jsonResponse = new JSONObject(omdbClient.getRaw(url));

        // If the season details were not found (due to occasional mismatch between TMDB and OMDB)
        if (jsonResponse.getString("Response").equals("False")) {
            // Make request to TMDB series details to fetch show title
            url = UriComponentsBuilder
                    .fromUriString("https://api.themoviedb.org/3/tv")
                    .pathSegment(showId)
                    .toUriString();
            jsonResponse = new JSONObject(tmdbClient.getRaw(url));
            season.setShowTitle(jsonResponse.optString("name"));
        } else {
            JSONArray episodes = jsonResponse.optJSONArray("Episodes");
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
        }

        // If the user is logged in, check whether season is on their ranking list
        User user = authService.retrieveUserFromJwt();
        if (user != null) {
            season.setOnRankingList(seasonRankingRepository.existsById(new SeasonRankingId(user.getId(), season.getId())));;
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

    public EpisodeDto getEpisodeDetails(String episodeNumber, String seasonNumber, String showId) {
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

        // Fetch the number of seasons in the show and number of episodes in the current season for next/previous episode buttons on frontend
        episode.setNumSeasons(jsonResponse.optInt("number_of_seasons"));
        JSONArray seasons = jsonResponse.optJSONArray("seasons");
        for (int i = 0; i < seasons.length(); i++) {
            JSONObject seasonNode = seasons.getJSONObject(i);
            if (seasonNode.optInt("season_number") == Integer.parseInt(seasonNumber) - 1) {
                episode.setNumEpisodesInPreviousSeason(seasonNode.optInt("episode_count"));
            } else if (seasonNode.optInt("season_number") == Integer.parseInt(seasonNumber)) {
                episode.setNumEpisodesInSeason(seasonNode.optInt("episode_count"));
                break;
            }
        }

        if (!jsonResponse.optString("Plot").equals("N/A") && jsonResponse.optBoolean("Plot")) {
            episode.setPlot(jsonResponse.optString("Plot"));
        }

        // If the user is logged in check if it's on their ranking list
        User user = authService.retrieveUserFromJwt();
        if (user != null) {
            episode.setOnRankingList(episodeRankingRepository.existsById(new  EpisodeRankingId(user.getId(), episode.getId())));
        }
        episode.setReviewDistribution(episodeReviewRepository.getEpisodeReviewDistribution(episode.getId()));
        return episode;
    }

    @Transactional
    public ShowReviewWithUserInfoDto addReviewToShow(Long showId, ShowReviewDto reviewDto) {
        User user = authService.retrieveUserFromJwt();

        // Delete existing review if it exists
        showReviewRepository.deleteByUserIdAndShowId(user.getId(), showId);
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
        review.setUserId(user.getId());
        review.setShowId(showId);
        showReviewRepository.save(review);

        // Increment number of reviews for user
        userRepository.incrementNumReviews(user.getId());

        return showReviewRepository.findByIdWithUserInfo(review.getId());
    }

    public Page<ShowReviewWithUserInfoDto> getShowReviews(Long showId, Pageable page) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;

        // Subtract 1 from provided page to align with 0-indexed pages, and ensure non-negative pages are requested
        Pageable modifiedPage = PageRequest.of(
                Math.max(0, page.getPageNumber() - 1),
                page.getPageSize(),
                page.getSort()
        );
        return showReviewRepository.findAllByShowId(showId, userId, modifiedPage);
    }

    public ShowReviewWithUserInfoDto getShowReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return showReviewRepository.findById(reviewId, userId);
    }

    @Transactional
    public void likeShowReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();
        Optional<ShowReview> reviewOpt = showReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find a show review with ID: " + reviewId);
        }
        ShowReview review = reviewOpt.get();

        // Check if the user has already liked the review, if so we throw an exception
        if (likedShowReviewsRepository.existsByUserIdAndReviewId(user.getId(), reviewId)) {
            throw new AlreadyLikedException("You have already liked this show review");
        }

        LikedShowReview likedReview = new LikedShowReview();
        likedReview.setUserId(user.getId());
        likedReview.setReviewId(reviewId);
        likedShowReviewsRepository.save(likedReview);
        showReviewRepository.incrementLikes(reviewId);

        // Add the like show review event to the activity table, except liking own review
        if (!review.getUserId().equals(user.getId())) {
            Activity likeEvent = new Activity();
            likeEvent.setUserId(review.getUserId());
            likeEvent.setActivityType(ActivityType.LIKE_SHOW_REVIEW.getDbValue());
            likeEvent.setExternalId(likedReview.getId());
            activitiesRepository.save(likeEvent);
        }
    }

    @Transactional
    public void unlikeShowReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();

        // Check if the review exists, and if so has the user liked it
        if (showReviewRepository.existsById(reviewId)) {

            Optional<LikedShowReview> likedReviewOpt = likedShowReviewsRepository.findByUserIdAndReviewId(user.getId(), reviewId);
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
    public void deleteShowReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();

        Optional<ShowReview> reviewOpt = showReviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            ShowReview review = reviewOpt.get();

            // Ensure that the review belongs to the requesting user
            if (!review.getUserId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You are not allowed to delete this show review");
            }
            showReviewRepository.delete(review);

            // Delete all occurrences of this from the activities table
            // In this instance it needs to delete the following
            // - Show Review Like Notifications
            // - Show Review Comment Notifications
            List<Integer> activityTypes = Arrays.asList(ActivityType.LIKE_SHOW_REVIEW.getDbValue(), ActivityType.COMMENT_SHOW_REVIEW.getDbValue());
            activitiesRepository.deleteShowReviewActivities(review.getId(), activityTypes);

            // Decrement the number of reviews for the user
            userRepository.decrementNumReviews(user.getId());
        }
    }

    @Transactional
    public void updateShowReview(Long reviewId, UpdateReviewDto updates) {
        User user = authService.retrieveUserFromJwt();

        ShowReview review = showReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ItemNotFoundException("Didn't find a show review with ID: " + reviewId));

        // Ensure that the review belongs to the requesting user
        if (!review.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not allowed to update this show review");
        }

        modelMapper.map(updates, review);
        showReviewRepository.save(review);
    }

    @Transactional
    public EpisodeReviewWithUserInfoDto addReviewToEpisode(Long episodeId, EpisodeReviewDto reviewDto) {
        User user = authService.retrieveUserFromJwt();

        // Delete existing review if it exists
        episodeReviewRepository.deleteByUserIdAndEpisodeId(user.getId(), episodeId);
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
        review.setUserId(user.getId());
        review.setNumLikes(0L);
        review.setReviewDate(new Date());
        episodeReviewRepository.save(review);

        // Increment the number of reviews for the user
        userRepository.incrementNumReviews(user.getId());

        return episodeReviewRepository.findByIdWithUserInfo(review.getId());
    }

    public Page<EpisodeReviewWithUserInfoDto> getEpisodeReviews(Long episodeId, Pageable page) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;

        // Subtract 1 from provided page to align with 0-indexed pages, and ensure non-negative pages are requested
        Pageable modifiedPage = PageRequest.of(
                Math.max(0, page.getPageNumber() - 1),
                page.getPageSize(),
                page.getSort()
        );
        return episodeReviewRepository.findAllByEpisodeId(episodeId, userId, modifiedPage);
    }

    public EpisodeReviewWithUserInfoDto getEpisodeReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return episodeReviewRepository.findById(reviewId, userId);
    }

    @Transactional
    public void likeEpisodeReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();
        Optional<EpisodeReview> reviewOpt = episodeReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find a episode review with ID: " + reviewId);
        }
        EpisodeReview review = reviewOpt.get();

        // Check if the user has already liked the review, if so we throw an exception
        if (likedEpisodeReviewsRepository.existsByUserIdAndReviewId(user.getId(), reviewId)) {
            throw new AlreadyLikedException("You have already liked this episode review");
        }

        LikedEpisodeReview likedReview =  new LikedEpisodeReview();
        likedReview.setUserId(user.getId());
        likedReview.setReviewId(reviewId);
        likedEpisodeReviewsRepository.save(likedReview);
        episodeReviewRepository.incrementLikes(reviewId);

        // Add the like episode review event to the activities table, except liking own review
        if (!review.getUserId().equals(user.getId())) {
            Activity likeEvent = new Activity();
            likeEvent.setUserId(review.getUserId());
            likeEvent.setActivityType(ActivityType.LIKE_EPISODE_REVIEW.getDbValue());
            likeEvent.setExternalId(likedReview.getId());
            activitiesRepository.save(likeEvent);
        }
    }

    @Transactional
    public void unlikeEpisodeReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();

        // Check if the review exists, and if so, ensure the user has liked it already
        if (episodeReviewRepository.existsById(reviewId)) {
            Optional<LikedEpisodeReview> likedReviewOpt = likedEpisodeReviewsRepository.findByUserIdAndReviewId(user.getId(), reviewId);
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

    @Transactional
    public void deleteEpisodeReview(Long reviewId) {
        User user = authService.retrieveUserFromJwt();

        Optional<EpisodeReview> reviewOpt = episodeReviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            EpisodeReview review = reviewOpt.get();

            // Ensure that the review belongs to the requesting user
            if (!review.getUserId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You are not allowed to delete this show review");
            }
            episodeReviewRepository.delete(review);

            // Delete all occurrences of this from the activities table
            // In this instance it needs to delete the following
            // - Episode Review Like Notifications
            // - Episode Review Comment Notifications
            List<Integer> activityTypes = Arrays.asList(ActivityType.LIKE_EPISODE_REVIEW.getDbValue(), ActivityType.COMMENT_EPISODE_REVIEW.getDbValue());
            activitiesRepository.deleteEpisodeReviewActivities(review.getId(), activityTypes);

            // Decrement the number of reviews for the user
            userRepository.decrementNumReviews(user.getId());
        }
    }

    @Transactional
    public void updateEpisodeReview(Long reviewId, UpdateReviewDto updates) {
        User user = authService.retrieveUserFromJwt();

        EpisodeReview review = episodeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ItemNotFoundException("Didn't find a episode review with ID: " + reviewId));

        // Ensure that the review belongs to the requesting user
        if (!review.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not allowed to update this episode review");
        }

        modelMapper.map(updates, review);
        episodeReviewRepository.save(review);
    }


    public ShowResultsPageDto getTrendingShows(Integer page) {
        // Make request to TMDB trending TV endpoint
        String url = UriComponentsBuilder
                .fromUriString("https://api.themoviedb.org/3/trending/tv/week")
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
    public ReviewCommentWithUserInfoDto addCommentToShowReview(Long reviewId, ReviewCommentDto reviewComment) {
        User user = authService.retrieveUserFromJwt();
        Optional<ShowReview> reviewOpt = showReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an review with ID: " + reviewId);
        }
        ShowReview review = reviewOpt.get();

        ShowReviewComment newComment = new ShowReviewComment();
        newComment.setUserId(user.getId());
        newComment.setReviewId(reviewId);
        newComment.setCommentText(reviewComment.getCommentText());
        showReviewCommentRepository.save(newComment);
        showReviewRepository.incrementNumComments(reviewId);

        // Add the show review comment event to activities table, except commenting on own review
        if (!review.getUserId().equals(user.getId())) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(review.getUserId());
            commentEvent.setActivityType(ActivityType.COMMENT_SHOW_REVIEW.getDbValue());
            commentEvent.setExternalId(newComment.getId());
            activitiesRepository.save(commentEvent);
        }
        return showReviewCommentRepository.findByIdWithUserInfo(newComment.getId());
    }

    public Page<ReviewCommentWithUserInfoDto> getShowReviewComments(Long reviewId, int page) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return showReviewCommentRepository.findAllByReviewId(reviewId, userId, PageRequest.of(page - 1, numComments));
    }

    public ReviewCommentWithUserInfoDto getShowReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return showReviewCommentRepository.findById(commentId, userId);
    }

    @Transactional
    public void likeShowReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();
        Optional<ShowReviewComment> commentOpt = showReviewCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an show review comment with ID: " + commentId);
        }
        ShowReviewComment comment = commentOpt.get();

        // Check if the user has already liked the comment
        if (likedShowReviewCommentsRepository.existsByUserIdAndCommentId(user.getId(), commentId)) {
            throw new AlreadyLikedException("You have already liked this show review comment");
        }

        LikedShowReviewComment likedComment = new LikedShowReviewComment();
        likedComment.setUserId(user.getId());
        likedComment.setCommentId(commentId);
        likedShowReviewCommentsRepository.save(likedComment);
        showReviewCommentRepository.incrementNumLikes(commentId);

        // Add the like show review comment event to the activities table, except liking own comment
        if (!comment.getUserId().equals(user.getId())) {
            Activity likeCommentEvent = new Activity();
            likeCommentEvent.setUserId(comment.getUserId());
            likeCommentEvent.setActivityType(ActivityType.LIKE_SHOW_REVIEW_COMMENT.getDbValue());
            likeCommentEvent.setExternalId(likedComment.getId());
            activitiesRepository.save(likeCommentEvent);
        }
    }

    @Transactional
    public void unlikeShowReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();

        if (showReviewCommentRepository.existsById(commentId)) {
            // Check to ensure the user has liked the comment
            Optional<LikedShowReviewComment> likedCommentOpt = likedShowReviewCommentsRepository.findByUserIdAndCommentId(user.getId(), commentId);
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
    public void deleteShowReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();

        Optional<ShowReviewComment> commentOpt = showReviewCommentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            ShowReviewComment comment = commentOpt.get();

            // Ensure that the comment belongs to the requesting user
            if (!comment.getUserId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You are not allowed to delete this show review comment");
            }

            // Delete all occurrences of this from the activities table
            // In this instance it needs to delete the following
            // - Show Review Comment Like Notifications
            // - Show Review Comment Notification
            List<Integer> activityTypes = Arrays.asList(ActivityType.LIKE_SHOW_REVIEW_COMMENT.getDbValue(), ActivityType.COMMENT_SHOW_REVIEW.getDbValue());
            activitiesRepository.deleteShowReviewCommentActivities(commentId, activityTypes);

            showReviewCommentRepository.delete(comment);
            showReviewRepository.decrementNumComments(comment.getReviewId());
        }
    }

    @Transactional
    public void updateShowReviewComment(Long commentId, UpdateCommentDto updates) {
        User user = authService.retrieveUserFromJwt();

        ShowReviewComment comment = showReviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new ItemNotFoundException("Couldn't find a show review comment with ID: " + commentId));

        // Ensure that the comment being edited belongs to the requesting user
        if (!comment.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not allowed to update this show review comment");
        }

        modelMapper.map(updates, comment);
        showReviewCommentRepository.save(comment);
    }


    @Transactional
    public ReviewCommentWithUserInfoDto addCommentToEpisodeReview(Long reviewId, ReviewCommentDto reviewComment) {
        User user = authService.retrieveUserFromJwt();
        Optional<EpisodeReview> reviewOpt = episodeReviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an episode review with ID: " + reviewId);
        }
        EpisodeReview review = reviewOpt.get();

        EpisodeReviewComment newComment = new EpisodeReviewComment();
        newComment.setUserId(user.getId());
        newComment.setReviewId(reviewId);
        newComment.setCommentText(reviewComment.getCommentText());
        episodeReviewCommentRepository.save(newComment);
        episodeReviewRepository.incrementNumComments(reviewId);

        // Add episode review comment event to activities table, except commenting on own review
        if (!review.getUserId().equals(user.getId())) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(review.getUserId());
            commentEvent.setActivityType(ActivityType.COMMENT_EPISODE_REVIEW.getDbValue());
            commentEvent.setExternalId(newComment.getId());
            activitiesRepository.save(commentEvent);
        }

        return episodeReviewCommentRepository.findByIdWithUserInfo(newComment.getId());
    }

    public Page<ReviewCommentWithUserInfoDto> getEpisodeReviewComments(Long reviewId, int page) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return episodeReviewCommentRepository.findAllByReviewId(reviewId, userId, PageRequest.of(page - 1, numComments));
    }

    public ReviewCommentWithUserInfoDto getEpisodeReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();
        Long userId = (user != null) ? user.getId() : null;
        return episodeReviewCommentRepository.findById(commentId, userId);
    }

    @Transactional
    public void likeEpisodeReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();
        Optional<EpisodeReviewComment> commentOpt = episodeReviewCommentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            throw new ItemNotFoundException("Didn't find an episode review comment with ID: " + commentId);
        }
        EpisodeReviewComment comment = commentOpt.get();

        // Check if the user has already liked the comment
        if (likedEpisodeReviewCommentsRepository.existsByUserIdAndCommentId(user.getId(), commentId)) {
            throw new AlreadyLikedException("You have already liked this episode review comment");
        }

        LikedEpisodeReviewComment likedComment = new LikedEpisodeReviewComment();
        likedComment.setUserId(user.getId());
        likedComment.setCommentId(commentId);
        likedEpisodeReviewCommentsRepository.save(likedComment);
        episodeReviewCommentRepository.incrementNumLikes(commentId);

        // Add the episode review comment like event to the activities table, except for liking own comment
        if (!comment.getUserId().equals(user.getId())) {
            Activity commentEvent = new Activity();
            commentEvent.setUserId(comment.getUserId());
            commentEvent.setActivityType(ActivityType.LIKE_EPISODE_REVIEW_COMMENT.getDbValue());
            commentEvent.setExternalId(likedComment.getId());
            activitiesRepository.save(commentEvent);
        }
    }

    @Transactional
    public void unlikeEpisodeReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();

        if (episodeReviewCommentRepository.existsById(commentId)) {
            Optional<LikedEpisodeReviewComment> likedCommentOpt = likedEpisodeReviewCommentsRepository.findByUserIdAndCommentId(user.getId(), commentId);
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

    @Transactional
    public void deleteEpisodeReviewComment(Long commentId) {
        User user = authService.retrieveUserFromJwt();

        Optional<EpisodeReviewComment> commentOpt = episodeReviewCommentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            EpisodeReviewComment comment = commentOpt.get();

            // Ensure that the comment belongs to the requesting user
            if (!comment.getUserId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You are not allowed to delete this show review comment");
            }

            // Delete all occurrences of this from the activities table
            // In this instance it needs to delete the following
            // - Episode Review Comment Like Notifications
            // - Episode Review Comment Notification
            List<Integer> activityTypes = Arrays.asList(ActivityType.LIKE_EPISODE_REVIEW_COMMENT.getDbValue(), ActivityType.COMMENT_EPISODE_REVIEW.getDbValue());
            activitiesRepository.deleteEpisodeReviewCommentActivities(commentId, activityTypes);

            episodeReviewCommentRepository.delete(comment);
            episodeReviewRepository.decrementNumComments(comment.getReviewId());
        }
    }

    @Transactional
    public void updateEpisodeReviewComment(Long commentId, UpdateCommentDto updates) {
        User user = authService.retrieveUserFromJwt();

        EpisodeReviewComment comment = episodeReviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new ItemNotFoundException("Couldn't find an episode review comment with ID: " + commentId));

        // Ensure that the comment being edited belongs to the requesting user
        if (!comment.getUserId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not allowed to update this show review comment");
        }

        modelMapper.map(updates, comment);
        episodeReviewCommentRepository.save(comment);
    }
}
