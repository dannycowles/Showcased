package com.example.showcased.dto;

import lombok.Data;

@Data
public class UpdateReviewDto {
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
}
