package com.example.showcased.service;

import com.example.showcased.dto.SearchDto;
import com.example.showcased.dto.SearchResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SearchService {

    @Value("${tmdbApi}")
    private String tmdbKey;


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
        ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SearchResponse.class);
        List<SearchDto> results = responseEntity.getBody().getResults();

        // For each of the search results, use another TMDB endpoint to retrieve the end year
        for (SearchDto result : results) {
            url = "https://api.themoviedb.org/3/tv/" + result.getId();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            // Update the start year to only year instead of full date
            result.setStartYear(result.getStartYear().split("-")[0]);

            // Parse the response and extract the last date
            String responseBody = response.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String endYear = jsonResponse.optString("last_air_date").split("-")[0];

            // Update the result object to include the end year
            result.setEndYear(endYear);
        }
        return results;
    }
}
