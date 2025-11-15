package com.example.showcased.repository;

import com.example.showcased.entity.LikedSeasonReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedSeasonReviewsRepository extends JpaRepository<LikedSeasonReview, Long> {
    boolean existsByReviewIdAndUserId(Long reviewId, Long id);

    Optional<LikedSeasonReview> findByReviewIdAndUserId(Long reviewId, Long id);
}
