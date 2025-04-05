package com.example.showcased.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private double rating;
    private String showTitle;
    private String commentary;
    private boolean containsSpoilers;
}
