package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class LikedShowReviewsId implements Serializable {
    private Long userId;
    private Long reviewId;
}
