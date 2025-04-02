package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShowDto {
    private String id;

    private String imdbId;

    private String imdbRating;

    private String imdbVotes;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("name")
    private String title;

    @JsonProperty("tagline")
    private String tagline;

    @JsonProperty("plot")
    private String plot;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("average_runtime")
    private String averageRuntime;

    @JsonProperty("first_air_date")
    private String startYear;

    @JsonProperty("last_air_date")
    private String endYear = "";

    @JsonProperty("number_of_episodes")
    private int numEpisodes;

    @JsonProperty("number_of_seasons")
    private int numSeasons;

    @JsonProperty("genres")
    private List<GenresDto> genres;

    @JsonProperty("created_by")
    private List<CreatorDto> creator;

    @JsonProperty("cast")
    private List<CastDto> cast;

    @JsonProperty("in_production")
    private boolean inProduction;

    private String awards;

    private List<WatchOptionDto> streamOptions = new ArrayList<>();

    private List<WatchOptionDto> buyOptions = new ArrayList<>();

    void setBackdropPath(String backdropPath) {
        if (backdropPath != null) {
            this.backdropPath = "https://image.tmdb.org/t/p/w1280" + backdropPath;
        } else {
            this.backdropPath = "default";
        }
    }

    void setPosterPath(String posterPath) {
        if (posterPath != null) {
            this.posterPath = "https://image.tmdb.org/t/p/w500" + posterPath;
        } else {
            this.posterPath = "default";
        }
    }

    void setStartYear(String startYear) {
        if (startYear != null) {
            this.startYear = startYear.split("-")[0];
        }
    }

    void setEndYear(String endYear) {
        if (!this.inProduction) {
            if (endYear != null) {
                this.endYear = endYear.split("-")[0];
            }
        }
    }
}
