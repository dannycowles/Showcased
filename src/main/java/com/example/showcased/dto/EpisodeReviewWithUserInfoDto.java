package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EpisodeReviewWithUserInfoDto {
    private Long id;
    private String username;
    private String profilePicture;
    private Long reviewerId;
    private Long showId;
    private int season;
    private int episode;
    private String showTitle;
    private String episodeTitle;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes;
    private Date reviewDate;
    private boolean likedByUser;
}
