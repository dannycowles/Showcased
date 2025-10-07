package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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
    private int numSeasons;
    private int numEpisodesInSeason;
    private int numEpisodesInPreviousSeason;

    @JsonProperty("isOnRankingList")
    private boolean isOnRankingList = false;
    private List<ReviewDistributionDto> reviewDistribution;

    public void setStillPath(String stillPath) {
        if (stillPath != null) {
            this.stillPath = "https://image.tmdb.org/t/p/original" + stillPath;
        }
    }
}
