package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "season_review_comments")
@NoArgsConstructor
public class SeasonReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reviewId;
    private Long userId;
    private String commentText;
    private Date createdAt = new Date();
    private int numLikes = 0;

    public SeasonReviewComment(Long reviewId, Long userId, String commentText) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.commentText = commentText;
    }
}
