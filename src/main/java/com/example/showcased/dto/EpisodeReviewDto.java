package com.example.showcased.dto;

import com.example.showcased.enums.ReviewType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeReviewDto extends ShowReviewDto {
    private String episodeTitle;
    private int season;
    private int episode;

    public EpisodeReviewDto(
            Long showId,
            double rating,
            String showTitle,
            String commentary,
            boolean containsSpoilers,
            String posterPath,
            Date reviewDate,
            Long numLikes,
            String episodeTitle,
            int season,
            int episode
    ) {
        super(ReviewType.EPISODE, showId, rating, showTitle, commentary, containsSpoilers, posterPath, reviewDate, numLikes);
        this.episodeTitle = episodeTitle;
        this.season = season;
        this.episode = episode;
    }
}
