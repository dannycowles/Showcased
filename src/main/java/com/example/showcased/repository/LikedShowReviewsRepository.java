package com.example.showcased.repository;

import com.example.showcased.entity.LikedShowReviews;
import com.example.showcased.entity.LikedShowReviewsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedShowReviewsRepository extends JpaRepository<LikedShowReviews, LikedShowReviewsId> {
    @Query("SELECT l.id.reviewId FROM LikedShowReviews l " +
            "JOIN ShowReview r ON r.showId = :showId " +
            "WHERE l.id.userId = :userId ")
    List<Long> findReviewIdsLikedByUserAndShow(@Param("userId")Long userId, @Param("showId") Long showId);
}
