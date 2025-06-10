package com.example.showcased.dto;

import lombok.Data;

@Data
public class EpisodeReviewDto {
    private double rating;
    private Long showId;
    private String showTitle;
    private String episodeTitle;
    private int season;
    private int episode;
    private String commentary;
    private boolean containsSpoilers;
    private String posterPath;
}
