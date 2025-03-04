package com.example.showcased.service;

import com.example.showcased.dto.CastResponseDto;
import com.example.showcased.dto.ShowDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
public class ShowService {

    @Value("${tmdbApi}")
    private String tmdbKey;

    @Value("${omdbApi}")
    private String omdbKey;

    public ShowDto getShowById(String id) {
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
}
