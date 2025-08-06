package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EpisodeReviewWithUserInfoDto {
    private String type;
    private Long id;
    private String username;
    private String profilePicture;
    private Long userId;
    private Long showId;
    private int season;
    private int episode;
    private String showTitle;
    private String episodeTitle;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes;
    private int numComments;
    private Date reviewDate;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;
    @JsonProperty("isOwnReview")
    private boolean isOwnReview;
}
