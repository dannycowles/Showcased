package com.example.showcased.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class SeasonReviewWithUserInfoDto extends ShowReviewWithUserInfoDto {
    private int season;

    public SeasonReviewWithUserInfoDto(
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
            boolean isOwnReview,
            int season
    ) {
        super(id, username, profilePicture, userId, showId, showTitle, rating, commentary, containsSpoilers, numLikes, numComments, reviewDate, isLikedByUser, isOwnReview);
        setType("SeasonPage");
        this.season = season;
    }
}
