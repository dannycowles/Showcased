package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowReviewWithUserInfoDto {
    private String type = "ShowPage";
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
    private int numComments;
    private Date reviewDate;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;
    @JsonProperty("isOwnReview")
    private boolean isOwnReview;

    public ShowReviewWithUserInfoDto(
            Long id,
            String username,
            String profilePicture,
            Long userId,
            Long showId,
            String showTitle,
            double rating,
            String commentary,
            boolean containsSpoilers,
            Long numLikes,
            int numComments,
            Date reviewDate,
            boolean isLikedByUser,
            boolean isOwnReview
    ) {
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.userId = userId;
        this.showId = showId;
        this.showTitle = showTitle;
        this.rating = rating;
        this.commentary = commentary;
        this.containsSpoilers = containsSpoilers;
        this.numLikes = numLikes;
        this.numComments = numComments;
        this.reviewDate = reviewDate;
        this.isLikedByUser = isLikedByUser;
        this.isOwnReview = isOwnReview;
    }

    protected void setType(String type) {
        this.type = type;
    }
}
