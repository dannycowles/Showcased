package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "liked_season_review_comments")
@NoArgsConstructor
public class LikedSeasonReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long commentId;
    private Date createdAt = new Date();

    public LikedSeasonReviewComment(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
