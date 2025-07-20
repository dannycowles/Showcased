package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityShowReviewCommentDto {
    private Long commentId;
    private Long reviewId;
    private Long showId;
    private String showTitle;
    private String commentText;
}
