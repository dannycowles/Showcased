package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "liked_episode_review_comments")
@AllArgsConstructor
@NoArgsConstructor
public class LikedEpisodeReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long commentId;
    private Date createdAt = new Date();
}
