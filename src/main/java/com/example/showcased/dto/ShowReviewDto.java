package com.example.showcased.dto;

import com.example.showcased.enums.ReviewType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ShowReviewDto {
    private ReviewType type = ReviewType.SHOW;
    private Long id;
    private Long showId;
    private double rating;
    private String showTitle;
    private String commentary;
    private boolean containsSpoilers;
    private String posterPath;
    private Date reviewDate = new Date();
    private Long numLikes = 0L;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;

    public ShowReviewDto(Long reviewId, Long showId, double rating, String showTitle, String commentary, boolean containsSpoilers, String posterPath, Date reviewDate, Long numLikes, boolean isLikedByUser) {
        this.showId = showId;
        this.id = reviewId;
        this.rating = rating;
        this.showTitle = showTitle;
        this.commentary = commentary;
        this.containsSpoilers = containsSpoilers;
        this.posterPath = posterPath;
        this.reviewDate = reviewDate;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
    }

    // Constructor with explicit ReviewType for other extending review types to use
    public ShowReviewDto(ReviewType type, Long reviewId, Long showId, double rating, String showTitle, String commentary, boolean containsSpoilers, String posterPath, Date reviewDate, Long numLikes, boolean isLikedByUser) {
        this.type = type;
        this.id = reviewId;
        this.showId = showId;
        this.rating = rating;
        this.showTitle = showTitle;
        this.commentary = commentary;
        this.containsSpoilers = containsSpoilers;
        this.posterPath = posterPath;
        this.reviewDate = reviewDate;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
    }
}
