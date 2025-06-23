package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
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

    @JsonAlias("backdrop_path")
    private String backdropPath;

    @JsonAlias("poster_path")
    private String posterPath;

    @JsonAlias("name")
    private String title;

    private String tagline;
    private String plot;
    private String rating;

    @JsonAlias("average_runtime")
    private String averageRuntime;

    @JsonAlias("first_air_date")
    private String startYear;

    @JsonAlias("last_air_date")
    private String endYear = "";

    @JsonAlias("number_of_episodes")
    private int numEpisodes;

    @JsonAlias("number_of_seasons")
    private int numSeasons;
    private List<GenresDto> genres;

    @JsonAlias("created_by")
    private List<CreatorDto> creators;
    private List<CastDto> cast;

    @JsonAlias("in_production")
    private boolean inProduction;

    private String awards;
    private List<WatchOptionDto> streamOptions = new ArrayList<>();
    private List<WatchOptionDto> buyOptions = new ArrayList<>();
    private List<SearchDto> recommendations = new ArrayList<>();

    private boolean onWatchlist = false;
    private boolean onWatchingList = false;
    private boolean onRankingList = false;

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
