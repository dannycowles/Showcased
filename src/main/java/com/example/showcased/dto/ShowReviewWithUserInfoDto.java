package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ShowReviewWithUserInfoDto {
    private Long id;
    private String username;
    private String profilePicture;
    private Long userId;
    private Long showId;
    private String showTitle;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes;
    private Date reviewDate;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;
}
