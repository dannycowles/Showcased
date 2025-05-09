package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.*;
import com.example.showcased.exception.AlreadyLikedShowReviewException;
import com.example.showcased.exception.HaventLikedShowReviewException;
import com.example.showcased.exception.InvalidPageException;
import com.example.showcased.repository.*;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ShowService {

    private final ModelMapper modelMapper;
    @Value("${tmdbApi}")
    private String tmdbKey;

    @Value("${omdbApi}")
    private String omdbKey;

    private final ReviewRepository reviewRepository;
    private final LikedReviewsRepository likedReviewsRepository;
    private final WatchlistRepository watchlistRepository;
    private final WatchingRepository watchingRepository;
    private final ShowRankingRepository showRankingRepository;
    private final SeasonRankingRepository seasonRankingRepository;

    public ShowService(ReviewRepository reviewRepository,
                       ModelMapper modelMapper,
                       LikedReviewsRepository likedReviewsRepository,
                       WatchlistRepository watchlistRepository,
                       WatchingRepository watchingRepository,
                       ShowRankingRepository showRankingRepository,
                       SeasonRankingRepository seasonRankingRepository) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.likedReviewsRepository = likedReviewsRepository;
        this.watchlistRepository = watchlistRepository;
        this.watchingRepository = watchingRepository;
        this.showRankingRepository = showRankingRepository;
        this.seasonRankingRepository = seasonRankingRepository;
    }

    public List<SearchDto> searchShows(String query) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make request to TMDB show search endpoint
        String url = "https://api.themoviedb.org/3/search/tv?query=" + query;
        ResponseEntity<SearchResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SearchResponseDto.class);
        List<SearchDto> results = responseEntity.getBody().getResults();

        // For each of the search results, use another TMDB endpoint to retrieve the end year
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (SearchDto searchResult : results) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String url2 = "https://api.themoviedb.org/3/tv/" + searchResult.getId();
                ResponseEntity<String> response = restTemplate.exchange(url2, HttpMethod.GET, requestEntity, String.class);

                // Parse the response and extract the last date
                JSONObject jsonResponse = new JSONObject(response.getBody());

                // Check if the show is in production, if so leave the last_air_date blank
                String endYear = "";
                if (!jsonResponse.optBoolean("in_production")) {
                    endYear = jsonResponse.optString("last_air_date").split("-")[0];
                }

                // Update the result object to include the end year
                searchResult.setEndYear(endYear);
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return results;
    }

    public ShowDto getShowDetails(String id, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make request to TMDB show details endpoint
        String url = "https://api.themoviedb.org/3/tv/" + id;
        ShowDto show = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ShowDto.class).getBody();

        // Make request to TMDB external ID endpoint
        url = "https://api.themoviedb.org/3/tv/" + id + "/external_ids";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract the IMDB id
        JSONObject jsonResponse = new JSONObject(response.getBody());
        String imdbId = jsonResponse.optString("imdb_id");
        show.setImdbId(imdbId);

        // Make request to TMDB cast endpoint
        url = "https://api.themoviedb.org/3/tv/" + id + "/credits";
        CastResponseDto cast = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CastResponseDto.class).getBody();

        // Store only the first 5 stars (can modify later)
        show.setCast(cast.getCast().stream().limit(5).collect(Collectors.toList()));

        // Make request to OMDB show endpoint using the IMDB id
        url = "https://www.omdbapi.com/?apikey=" + omdbKey + "&i=" + imdbId;
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract the following information:
        // - Plot
        // - Rated
        // - Runtime
        // - imdbRating
        // - imdbVotes
        // - Awards
        jsonResponse = new JSONObject(response.getBody());
        show.setPlot(jsonResponse.optString("Plot"));
        show.setRating(jsonResponse.optString("Rated"));
        show.setAverageRuntime(jsonResponse.optString("Runtime"));
        show.setImdbRating(jsonResponse.optString("imdbRating"));
        show.setImdbVotes(jsonResponse.optString("imdbVotes"));
        show.setAwards(jsonResponse.optString("Awards"));

        // Make request to TMDB watch providers endpoint
        url = "https://api.themoviedb.org/3/tv/" + id + "/watch/providers";
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        jsonResponse = new JSONObject(response.getBody());

        // Retrieve the US based results for simplicity
        JSONObject results = jsonResponse.optJSONObject("results");
        results = results.optJSONObject("US");
        if (results == null) {
            return show;
        }

        // Parse and set the streaming and buy options for the show
        JSONArray streaming = results.optJSONArray("flatrate");
        List<WatchOptionDto> streamingOptions = new ArrayList<>();
        if (streaming != null) {
            for (int i = 0; i < streaming.length(); i++) {
                JSONObject streamingOption = streaming.getJSONObject(i);
                streamingOptions.add(new WatchOptionDto(streamingOption.optString("provider_name"), streamingOption.optString("logo_path")));
            }
        }

        JSONArray buy = results.optJSONArray("buy");
        List<WatchOptionDto> buyOptions = new ArrayList<>();
        if (buy != null) {
            for (int i = 0; i < buy.length(); i++) {
                JSONObject buyOption = buy.optJSONObject(i);
                buyOptions.add(new WatchOptionDto(buyOption.optString("provider_name"), buyOption.optString("logo_path")));
            }
        }

        show.setStreamOptions(streamingOptions);
        show.setBuyOptions(buyOptions);

        // Check if the user is logged in, if so, check if the show is on watchlist/watching/ranking
        Long userId = (Long) session.getAttribute("user");
        if (userId != null) {
            show.setOnWatchlist(watchlistRepository.existsById(new WatchId(userId, Long.parseLong(id))));
            show.setOnWatchingList(watchingRepository.existsById(new WatchId(userId, Long.parseLong(id))));
            show.setOnRankingList(showRankingRepository.existsById(new WatchId(userId, Long.parseLong(id))));
        }

        return show;
    }

    public NumSeasonsDto getNumberOfSeasons(int showId) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        String url = "https://api.themoviedb.org/3/tv/" + showId;
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, NumSeasonsDto.class).getBody();
    }

    public SeasonDto getSeasonDetails(int seasonNumber, int showId, HttpSession session) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make a request to TMDB season details endpoint
        String url = "https://api.themoviedb.org/3/tv/" + showId + "/season/" + seasonNumber;
        SeasonDto season = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SeasonDto.class ).getBody();

        // Make request to TMDB external ID endpoint
        url = "https://api.themoviedb.org/3/tv/" + showId + "/external_ids";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract the IMDB id
        JSONObject jsonResponse = new JSONObject(response.getBody());
        String imdbId = jsonResponse.optString("imdb_id");

        // Make a request to OMDB endpoint to retrieve IMDB ratings
        url = "https://www.omdbapi.com/?apikey=" + omdbKey + "&i=" + imdbId + "&Season=" + seasonNumber;
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract the array of episodes
        jsonResponse = new JSONObject(response.getBody());
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
        for (SeasonEpisodeDto seasonEpisode : seasonEpisodes) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String url2 = "https://www.omdbapi.com/?apikey=" + omdbKey + "&i=" + imdbId + "&Season=" + seasonNumber + "&Episode=" + seasonEpisode.getEpisodeNumber();
                ResponseEntity<String> response2 = restTemplate.exchange(url2, HttpMethod.GET, requestEntity, String.class);
                JSONObject jsonResponse2 = new JSONObject(response2.getBody());
                if (!jsonResponse2.optString("Plot").equals("N/A") && jsonResponse2.optBoolean("Plot")) {
                    seasonEpisode.setPlot(jsonResponse2.optString("Plot"));
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

    public EpisodeDto getEpisodeDetails(String episodeNumber, String seasonNumber, String showId) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make a request to TMDB episode endpoint
        String url = "https://api.themoviedb.org/3/tv/" +  showId + "/season/" + seasonNumber + "/episode/" + episodeNumber;
        EpisodeDto episode = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EpisodeDto.class).getBody();

        // Make request to TMDB external ID endpoint
        url = "https://api.themoviedb.org/3/tv/" + showId + "/external_ids";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract the IMDB id
        JSONObject jsonResponse = new JSONObject(response.getBody());
        String imdbId = jsonResponse.optString("imdb_id");

        // Make request to OMDB endpoint to get additional episode information
        url = "https://www.omdbapi.com/?apikey=" + omdbKey + "&i=" + imdbId + "&Season=" + seasonNumber + "&Episode=" + episodeNumber;
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // Parse the response and extract:
        // - IMDB rating
        // - IMDB votes
        // - Plot
        jsonResponse = new JSONObject(response.getBody());
        episode.setImdbVotes(jsonResponse.optString("imdbVotes"));
        episode.setImdbRating(jsonResponse.optString("imdbRating"));

        // Make request to TMDB show endpoint to retrieve show title
        url = "https://api.themoviedb.org/3/tv/" + showId;
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        jsonResponse = new JSONObject(response.getBody());
        episode.setShowTitle(jsonResponse.optString("name"));

        if (!jsonResponse.optString("Plot").equals("N/A") && jsonResponse.optBoolean("Plot")) {
            String plot = jsonResponse.optString("Plot");
            episode.setPlot(plot);
        }
        return episode;
    }

    public void addReviewToShow(Long id, ReviewDto review, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        ReviewId reviewId = new ReviewId(userId, id);

        // Delete existing review if it exists
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
        }

        Review newReview = modelMapper.map(review, Review.class);
        newReview.setId(reviewId);
        reviewRepository.save(newReview);
    }

    public List<ReviewWithUserInfoDto> getShowReviews(Long showId, HttpSession session) {
        List<ReviewWithUserInfoDto> reviews = reviewRepository.findAllByShowId(showId);

        // If the user is logged in, we will check which review(s) are liked by them and update the list as necessary
        Long userId = (Long) session.getAttribute("user");
        if (userId != null) {
            List<Long> likedReviewIds = likedReviewsRepository.findReviewIdsLikedByUserAndShow(userId, showId);
            for (ReviewWithUserInfoDto review : reviews) {
                if (likedReviewIds.contains(review.getId())) {
                    review.setLikedByUser(true);
                }
            }
        }
        return reviews;
    }

    public void likeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check if the user has already liked the review, if so we throw an exception
        LikedReviews likedReview = new LikedReviews(new LikedReviewsId(userId, reviewId));
        if (likedReviewsRepository.existsById(likedReview.getId())) {
            throw new AlreadyLikedShowReviewException("You have already liked this show review");
        }

        // Store the review like information in the DB
        likedReviewsRepository.save(likedReview);

        // Retrieve the review from the repository and increment the like count
        Review review = reviewRepository.findByReviewId(reviewId);
        review.setNumLikes(review.getNumLikes() + 1);
        reviewRepository.save(review);
    }

    public void unlikeShowReview(Long reviewId, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");

        // Check if the user has liked the review, if they haven't they can't unlike it so
        // we throw an exception
        LikedReviews likedReview = new LikedReviews(new LikedReviewsId(userId, reviewId));
        if (!likedReviewsRepository.existsById(likedReview.getId())) {
            throw new HaventLikedShowReviewException("You have not liked this show review");
        }

        // Delete the review like information from the DB
        likedReviewsRepository.delete(likedReview);

        // Retrieve the review from the repository and decrement the like count
        Review review = reviewRepository.findByReviewId(reviewId);
        review.setNumLikes(review.getNumLikes() - 1);
        reviewRepository.save(review);
    }

    public TrendingShowsDto getTrendingShows(Integer page) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tmdbKey);

        // Create HTTP entity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make request to TMDB trending TV endpoint
        if (page == null) {
            page = 1;
        }
        String url = "https://api.themoviedb.org/3/trending/tv/day?page=" + page;
        TrendingShowsDto shows = null;
        try {
            shows =  restTemplate.exchange(url, HttpMethod.GET, requestEntity, TrendingShowsDto.class).getBody();
        } catch(HttpClientErrorException ex) {
            String errorBody = ex.getResponseBodyAsString();
            JSONObject jsonObject = new JSONObject(errorBody);
            throw new InvalidPageException(jsonObject.getString("status_message"));
        }

        // For each of the search results, use another TMDB endpoint to retrieve the end year
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (SearchDto searchResult : shows.getResults()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String url2 = "https://api.themoviedb.org/3/tv/" + searchResult.getId();
                ResponseEntity<String> response = restTemplate.exchange(url2, HttpMethod.GET, requestEntity, String.class);

                // Parse the response and extract the last date
                JSONObject jsonResponse = new JSONObject(response.getBody());

                // Check if the show is in production, if so leave the last_air_date blank
                String endYear = "";
                if (!jsonResponse.optBoolean("in_production")) {
                    endYear = jsonResponse.optString("last_air_date").split("-")[0];
                }

                // Update the result object to include the end year
                searchResult.setEndYear(endYear);
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return shows;
    }
}
