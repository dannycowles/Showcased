package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowDto {
    private String id;

    private String imdbId;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("name")
    private String title;

    @JsonProperty("first_air_date")
    private String startYear;

    @JsonProperty("last_air_date")
    private String endYear;

    @JsonProperty("number_of_episodes")
    private int numEpisodes;

    @JsonProperty("number_of_seasons")
    private int numSeasons;

    @JsonProperty("created_by")
    private List<CreatorDto> creator;

    @JsonProperty("cast")
    private List<CastDto> cast;

    void setBackdropPath(String backdropPath) {
        this.backdropPath = "https://image.tmdb.org/t/p/w1280" + backdropPath;
    }

    void setPosterPath(String posterPath) {
        this.posterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
    }
}
