package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDistributionDto {
    private Long rating;
    private Long numReviews;
}
