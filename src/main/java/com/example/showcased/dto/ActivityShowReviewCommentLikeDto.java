package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityShowReviewCommentLikeDto extends ActivityShowReviewCommentDto {
    private ActivityUserDto reviewUser;
    @JsonProperty("isOwnComment")
    private Boolean isOwnComment;

    public ActivityShowReviewCommentLikeDto(
            Long commentId,
            Long reviewId,
            Long showId,
            String showTitle,
            String commentText,
            Long userId,
            String username,
            String profilePicture,
            Boolean isOwnComment
    ) {
        super(commentId, reviewId, showId, showTitle, commentText);
        this.reviewUser = new ActivityUserDto(userId, username, profilePicture);
        this.isOwnComment = isOwnComment;
    }
}
