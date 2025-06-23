package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class SeasonDto {
    private String showTitle;
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

    public void setPosterPath(String posterPath) {
        this.posterPath = "https://image.tmdb.org/t/p/w342" + posterPath;
    }
}
