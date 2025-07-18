package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityShowReviewLikeDto {
    private Long reviewId;
    private Long showId;
    private String showTitle;
}
