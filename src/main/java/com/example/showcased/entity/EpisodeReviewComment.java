package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "episode_review_comments")
public class EpisodeReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reviewId;
    private Long userId;
    private String commentText;
    private Date createdAt = new Date();
    private int numLikes = 0;
}
