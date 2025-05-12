package com.example.showcased.service;

import com.example.showcased.exception.InvalidPageException;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class OMDBClient {

    private final RestTemplate restTemplate;

    public OMDBClient() {
        restTemplate = new RestTemplate();
    }

    public String getRaw(String url) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
        } catch (HttpClientErrorException ex) {
            JSONObject jsonResponse = new JSONObject(ex.getResponseBodyAsString());
            throw new InvalidPageException(jsonResponse.getString("status_message"));
        }
    }
}
