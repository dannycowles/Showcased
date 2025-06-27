package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class SeasonPartialDto {
    private Long id;
    private Long showId;
    @JsonAlias("season_number")
    private int season;
    @JsonAlias("poster_path")
    private String posterPath;
    private String showTitle;

    public void setPosterPath(String posterPath) {
        this.posterPath = "https://image.tmdb.org/t/p/w342" + posterPath;
    }
}
