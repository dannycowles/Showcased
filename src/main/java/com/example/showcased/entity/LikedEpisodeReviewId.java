package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class LikedEpisodeReviewId implements Serializable {
    private Long userId;
    private Long reviewId;
}
