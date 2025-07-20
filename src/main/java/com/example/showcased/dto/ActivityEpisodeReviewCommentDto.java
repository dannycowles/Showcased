package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityEpisodeReviewCommentDto extends ActivityEpisodeReviewLikeDto {
    private String commentText;

    public ActivityEpisodeReviewCommentDto(
            Long reviewId,
            Long showId,
            String showTitle,
            Integer season,
            Integer episode,
            String episodeTitle,
            String commentText
    ) {
        super(reviewId, showId, showTitle, season, episode, episodeTitle);
        this.commentText = commentText;
    }
}
