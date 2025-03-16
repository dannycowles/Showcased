package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorDto {
    private String id;
    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    void setProfilePath(String profilePath) {
        if (profilePath != null) {
            this.profilePath = "https://image.tmdb.org/t/p/w185" + profilePath;
        } else {
            this.profilePath = "default";
        }
    }
}
