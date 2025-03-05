package com.example.showcased.service;

import com.example.showcased.dto.EpisodeDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EpisodeService {

    @Value("${tmdbApi}")
    private String tmdbKey;

    @Value("${omdbApi}")
    private String omdbKey;

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
}
