package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SeasonDto {
    private String showTitle;
    private Long id;
    @JsonProperty("season_number")
    private int seasonNumber;
    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("episodes")
    private List<SeasonEpisodeDto> episodes;
    private boolean onRankingList = false;

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
