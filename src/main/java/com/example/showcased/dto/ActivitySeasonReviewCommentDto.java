package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivitySeasonReviewCommentDto extends ActivitySeasonReviewLikeDto {
    private Long commentId;

    public ActivitySeasonReviewCommentDto(Long reviewId, Long showId, String showTitle, Integer season, Long commentId) {
        super(reviewId, showId, showTitle, season);
        this.commentId = commentId;
    }
}
