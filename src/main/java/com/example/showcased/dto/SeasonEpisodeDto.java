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

    @JsonAlias("vote_average")
    private String imdbRating;

    public void setStillPath(String stillPath) {
        if (stillPath != null) {
            this.stillPath = "https://image.tmdb.org/t/p/original" + stillPath;
        } else {
            this.stillPath = "no-still.svg";
        }
    }
}
