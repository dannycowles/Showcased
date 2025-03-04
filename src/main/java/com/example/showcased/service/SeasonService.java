package com.example.showcased.service;

import com.example.showcased.dto.SeasonDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SeasonService {

    @Value("${tmdbApi}")
    private String tmdbKey;

    @Value("${omdbApi}")
    private String omdbKey;

    public SeasonDto getSeasonByNumberAndShowId(int seasonNumber, int showId) {
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
        return season;
    }
}
