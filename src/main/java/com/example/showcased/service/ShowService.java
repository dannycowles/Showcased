package com.example.showcased.service;

import com.example.showcased.dto.*;
import com.example.showcased.entity.Review;
import com.example.showcased.repository.ReviewRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Value("${tmdbApi}")
    private String tmdbKey;

    @Value("${omdbApi}")
    private String omdbKey;

    private final ReviewRepository reviewRepository;

    public ShowService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
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
                String endYear = jsonResponse.optString("last_air_date").split("-")[0];

                // Update the result object to include the end year
                searchResult.setEndYear(endYear);
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return results;
    }

    public ShowDto getShowDetails(String id) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);

        // Create HttpEntity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make request to TMDB show search endpoint
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
        jsonResponse = new JSONObject(response.getBody());
        String plot = jsonResponse.optString("Plot");
        String rating = jsonResponse.optString("Rated");
        String averageRuntime = jsonResponse.optString("Runtime");
        String imdbRating = jsonResponse.optString("imdbRating");
        String imdbVotes = jsonResponse.optString("imdbVotes");

        // Update the information in the show object
        show.setPlot(plot);
        show.setRating(rating);
        show.setAverageRuntime(averageRuntime);
        show.setImdbRating(imdbRating);
        show.setImdbVotes(imdbVotes);

        return show;
    }

    public SeasonDto getSeasonDetails(int seasonNumber, int showId) {
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
            season.setRating(i + 1, rating);
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
                seasonEpisode.setPlot(jsonResponse2.optString("Plot"));
            });
            futures.add(future);
        }
        // Wait for all API calls to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        season.setEpisodes(seasonEpisodes);

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
        String imdbVotes = jsonResponse.optString("imdbVotes");
        String imdbRating = jsonResponse.optString("imdbRating");
        String plot = jsonResponse.optString("Plot");
        episode.setImdbVotes(imdbVotes);
        episode.setImdbRating(imdbRating);
        episode.setPlot(plot);

        return episode;
    }

    public void addReviewToShow(Review review) {
        reviewRepository.save(review);
    }

    public List<ReviewWithUserInfoDto> getShowReviews(Long showId) {
        return reviewRepository.findAllByShowId(showId);
    }
}
