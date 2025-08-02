package com.example.showcased.dto;

import com.example.showcased.enums.ReviewType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ShowReviewDto {
    private ReviewType type = ReviewType.SHOW;
    private Long showId;
    private double rating;
    private String showTitle;
    private String commentary;
    private boolean containsSpoilers;
    private String posterPath;
    private Date reviewDate;

    public ShowReviewDto(Long showId, double rating, String showTitle, String commentary, boolean containsSpoilers, String posterPath, Date reviewDate) {
        this.showId = showId;
        this.rating = rating;
        this.showTitle = showTitle;
        this.commentary = commentary;
        this.containsSpoilers = containsSpoilers;
        this.posterPath = posterPath;
        this.reviewDate = reviewDate;
    }

    // Constructor with explicit ReviewType for other extending review types to use
    public ShowReviewDto(ReviewType type, Long showId, double rating, String showTitle, String commentary, boolean containsSpoilers, String posterPath, Date reviewDate) {
        this.type = type;
        this.showId = showId;
        this.rating = rating;
        this.showTitle = showTitle;
        this.commentary = commentary;
        this.containsSpoilers = containsSpoilers;
        this.posterPath = posterPath;
        this.reviewDate = reviewDate;
    }
}
