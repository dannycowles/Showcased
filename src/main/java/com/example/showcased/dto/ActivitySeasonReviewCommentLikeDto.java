package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivitySeasonReviewCommentLikeDto extends ActivitySeasonReviewCommentDto {
    private ActivityUserDto reviewUser;
    @JsonProperty("isOwnComment")
    private Boolean isOwnComment;

    public ActivitySeasonReviewCommentLikeDto(Long reviewId, Long showId, String showTitle, Integer season, Long commentId, Long userId, String username, String profilePicture, Boolean isOwnComment) {
        super(reviewId, showId, showTitle, season, commentId);
        this.reviewUser = new ActivityUserDto(userId, username, profilePicture);
        this.isOwnComment = isOwnComment;
    }
}
