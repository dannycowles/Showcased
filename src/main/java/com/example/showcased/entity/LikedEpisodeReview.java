package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "liked_episode_reviews")
public class LikedEpisodeReview {
    @EmbeddedId
    private LikedEpisodeReviewId id;
}
