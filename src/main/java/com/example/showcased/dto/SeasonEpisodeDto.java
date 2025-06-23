package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonEpisodeDto {
    @JsonAlias("air_date")
    private String airDate;

    @JsonAlias("episode_number")
    private int episodeNumber;

    private String name;

    @JsonAlias("overview")
    private String plot;

    @JsonAlias("still_path")
    private String stillPath;

    private String imdbRating;

    public void setStillPath(String stillPath) {
        this.stillPath = "https://image.tmdb.org/t/p/w185" + stillPath;
    }
}
