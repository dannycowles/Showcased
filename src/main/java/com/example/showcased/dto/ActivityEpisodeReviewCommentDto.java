package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityEpisodeReviewCommentDto extends ActivityEpisodeReviewLikeDto {
    private Long commentId;
    private String commentText;

    public ActivityEpisodeReviewCommentDto(
            Long reviewId,
            Long showId,
            String showTitle,
            Integer season,
            Integer episode,
            String episodeTitle,
            Long commentId,
            String commentText
    ) {
        super(reviewId, showId, showTitle, season, episode, episodeTitle);
        this.commentId = commentId;
        this.commentText = commentText;
    }
}
