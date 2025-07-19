package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityShowReviewCommentDto {
    private Long commentId;
    private Long reviewId;
    private Long showId;
    private String showTitle;
    private String commentText;
}
