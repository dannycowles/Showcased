package com.example.showcased.dto;

import lombok.Data;

@Data
public class UpdateReviewDto {
    private int rating;
    private String commentary;
    private boolean containsSpoilers;
}
