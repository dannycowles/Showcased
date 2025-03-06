package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonEpisodeDto {
    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("episode_number")
    private String episodeNumber;

    private String name;
    private String plot;

    @JsonProperty("still_path")
    private String stillPath;

    private String imdbRating;

    public void setStillPath(String stillPath) {
        this.stillPath = "https://image.tmdb.org/t/p/w185" + stillPath;
    }
}
