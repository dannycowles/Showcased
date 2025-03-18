package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SeasonDto {
    @JsonProperty("season_number")
    private int seasonNumber;
    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("episodes")
    private List<SeasonEpisodeDto> episodes;

    public void setRating(int episodeNumber, String rating) {
        for (SeasonEpisodeDto seasonEpisodeDto : episodes) {
            if (seasonEpisodeDto.getEpisodeNumber() == episodeNumber) {
                seasonEpisodeDto.setImdbRating(rating);
            }
        }
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = "https://image.tmdb.org/t/p/w342" + posterPath;
    }
}
