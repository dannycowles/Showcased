package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchDto {

    private String id;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("name")
    private String title;

    @JsonProperty("first_air_date")
    private String startYear;

    @JsonProperty("last_air_date")
    private String endYear;

    public SearchDto() {}

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
}
