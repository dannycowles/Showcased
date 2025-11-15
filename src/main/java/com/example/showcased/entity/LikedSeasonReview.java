package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "liked_season_reviews")
public class LikedSeasonReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long reviewId;
    private Date createdAt = new Date();

    public LikedSeasonReview(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
