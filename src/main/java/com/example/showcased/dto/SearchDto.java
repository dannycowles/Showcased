package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private String id;

    @JsonAlias("poster_path")
    private String posterPath;

    @JsonAlias("name")
    private String title;

    @JsonAlias("first_air_date")
    private String startYear;

    @JsonAlias("last_air_date")
    private String endYear;

    public void setPosterPath(String posterPath) {
        if (posterPath != null) {
            this.posterPath = "https://image.tmdb.org/t/p/w185" + posterPath;
        } else {
            this.posterPath = "default";
        }
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear.split("-")[0];
    }
}
