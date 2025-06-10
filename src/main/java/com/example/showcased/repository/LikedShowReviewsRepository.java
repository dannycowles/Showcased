package com.example.showcased.repository;

import com.example.showcased.entity.LikedShowReviews;
import com.example.showcased.entity.LikedShowReviewsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedShowReviewsRepository extends JpaRepository<LikedShowReviews, LikedShowReviewsId> {
}
