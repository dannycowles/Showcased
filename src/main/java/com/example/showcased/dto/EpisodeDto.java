package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EpisodeDto {
    private Long id;
    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("episode_number")
    private String episodeNumber;

    @JsonProperty("name")
    private String episodeTitle;

    private String showTitle;

    @JsonProperty("overview")
    private String plot;

    @JsonProperty("still_path")
    private String stillPath;

    private String imdbRating;
    private String imdbVotes;
    private int runtime;

    public void setStillPath(String stillPath) {
        this.stillPath = "https://image.tmdb.org/t/p/w780" + stillPath;
    }
}
