package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityEpisodeReviewCommentLikeDto extends ActivityEpisodeReviewCommentDto {
    private ActivityUserDto reviewUser;
    @JsonProperty("isOwnComment")
    private Boolean isOwnComment;

    public ActivityEpisodeReviewCommentLikeDto(
            Long reviewId,
            Long showId,
            String showTitle,
            Integer season,
            Integer episode,
            String episodeTitle,
            Long commentId,
            String commentText,
            Long userId,
            String username,
            String profilePicture,
            Boolean isOwnComment
    ) {
        super(reviewId, showId, showTitle, season, episode, episodeTitle, commentId, commentText);
        this.reviewUser = new ActivityUserDto(userId, username, profilePicture);
        this.isOwnComment = isOwnComment;
    }
}
