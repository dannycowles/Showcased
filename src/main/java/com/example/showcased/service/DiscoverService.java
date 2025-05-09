package com.example.showcased.service;

import com.example.showcased.dto.SearchDto;
import com.example.showcased.dto.TrendingShowsDto;
import com.example.showcased.exception.InvalidPageException;
import org.json.JSONObject;
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

@Service
public class DiscoverService {

    @Value("${tmdbApi}")
    private String tmdbKey;

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
