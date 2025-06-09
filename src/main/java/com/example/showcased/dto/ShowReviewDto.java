package com.example.showcased.dto;

import lombok.Data;

@Data
public class ShowReviewDto {
    private double rating;
    private String showTitle;
    private String commentary;
    private boolean containsSpoilers;
    private String posterPath;
}
