package com.example.showcased.service;

import com.example.showcased.exception.InvalidPageException;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class TMDBClient {

    private final RestTemplate restTemplate;
    private final HttpEntity<?> requestEntity;

    public TMDBClient(@Value("${tmdbApi}") String tmdbKey) {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + tmdbKey);
        requestEntity = new HttpEntity<>(headers);
    }

    public <T> T get(String url, Class<T> responseType) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType).getBody();
        } catch (HttpClientErrorException ex) {
            JSONObject jsonResponse = new JSONObject(ex.getResponseBodyAsString());
            throw new InvalidPageException(jsonResponse.getString("status_message"));
        }
    }

    public String getRaw(String url) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        } catch (HttpClientErrorException ex) {
            JSONObject jsonResponse = new JSONObject(ex.getResponseBodyAsString());
            throw new InvalidPageException(jsonResponse.getString("status_message"));
        }
    }
}
