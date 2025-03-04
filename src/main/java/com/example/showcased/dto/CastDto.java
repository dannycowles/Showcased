package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CastDto {
    private String id;
    private String name;
    private String character;

    @JsonProperty("profile_path")
    private String profilePath;

    void setProfilePath(String profilePath) {
        this.profilePath = "https://image.tmdb.org/t/p/w185" +profilePath;
    }
}
