package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class ShowEpisodesDto {
    private List<SeasonEpisode> episodes;

    @Data
    private static class SeasonEpisode {
        private int id;
        @JsonAlias("episode_number")
        private int episodeNumber;
        private String name;
        @JsonAlias("still_path")
        private String stillPath;

        public void setStillPath(String stillPath) {
            this.stillPath = "https://image.tmdb.org/t/p/w185" + stillPath;
        }
    }
}
