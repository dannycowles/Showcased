package com.example.showcased.dto;

import com.example.showcased.enums.ReviewType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class SeasonReviewDto extends ShowReviewDto {
    private int season;

    public SeasonReviewDto(
            Long reviewId,
            Long showId,
            double rating,
            String showTitle,
            String commentary,
            boolean containsSpoilers,
            String posterPath,
            Date reviewDate,
            Long numLikes,
            boolean isLikedByUser,
            int season
    ) {
        super(ReviewType.SEASON, reviewId, showId, rating, showTitle, commentary, containsSpoilers, posterPath, reviewDate, numLikes, isLikedByUser);
        this.season = season;
    }
}
