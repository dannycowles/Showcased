package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class EpisodeDto {
    private Long id;
    
    @JsonAlias("air_date")
    private String airDate;

    @JsonAlias("episode_number")
    private String episodeNumber;

    @JsonAlias("name")
    private String episodeTitle;

    private String showTitle;

    @JsonAlias("overview")
    private String plot;

    @JsonAlias("still_path")
    private String stillPath;

    private String imdbRating;
    private String imdbVotes;
    private int runtime;

    public void setStillPath(String stillPath) {
        this.stillPath = "https://image.tmdb.org/t/p/w780" + stillPath;
    }
}
