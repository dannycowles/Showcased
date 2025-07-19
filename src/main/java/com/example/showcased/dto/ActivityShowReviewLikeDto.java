package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityShowReviewLikeDto {
    private Long reviewId;
    private Long showId;
    private String showTitle;
}
