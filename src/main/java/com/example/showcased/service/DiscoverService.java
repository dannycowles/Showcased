package com.example.showcased.service;

import com.example.showcased.dto.TrendingShowsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscoverService {

    @Value("${tmdbApi}")
    private String tmdbKey;

    public TrendingShowsDto getTrendingShows(Integer page, Integer limit) {
        RestTemplate restTemplate = new RestTemplate();

        // Define headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Authorization", "Bearer " + tmdbKey);

        // Create HTTP entity with headers
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Make request to TMDB trending TV endpoint
        String url = "https://api.themoviedb.org/3/trending/tv/day";
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, TrendingShowsDto.class).getBody();
    }

}
