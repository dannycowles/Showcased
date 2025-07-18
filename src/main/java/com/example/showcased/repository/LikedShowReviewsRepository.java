package com.example.showcased.repository;

import com.example.showcased.entity.LikedShowReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedShowReviewsRepository extends JpaRepository<LikedShowReview, Long> {
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
}
