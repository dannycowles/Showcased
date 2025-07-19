package com.example.showcased.repository;

import com.example.showcased.entity.LikedEpisodeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedEpisodeReviewsRepository extends JpaRepository<LikedEpisodeReview, Long> {
    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    Optional<LikedEpisodeReview> findByUserIdAndReviewId(Long userId, Long reviewId);
}
