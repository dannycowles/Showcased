package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class SeasonDto {
    private String showTitle;

    @JsonAlias("air_date")
    private String airYear;
    private Long id;

    @JsonAlias("season_number")
    private int seasonNumber;
    private String overview;

    @JsonAlias("poster_path")
    private String posterPath;

    @JsonAlias("episodes")
    private List<SeasonEpisodeDto> episodes;
    private boolean onRankingList = false;

    public void setRating(int episodeNumber, String rating) {
        for (SeasonEpisodeDto seasonEpisodeDto : episodes) {
            if (seasonEpisodeDto.getEpisodeNumber() == episodeNumber) {
                seasonEpisodeDto.setImdbRating(rating);
            }
        }
    }

    public void setAirYear(String airYear) {
        this.airYear = airYear.split("-")[0];
    }

    public void setPosterPath(String posterPath) {
        if (posterPath != null) {
            this.posterPath = "https://image.tmdb.org/t/p/original" + posterPath;
        } else {
            this.posterPath = "no-poster-full.svg";
        }
    }
}
