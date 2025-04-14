package com.example.showcased.repository;

import com.example.showcased.entity.LikedReviews;
import com.example.showcased.entity.LikedReviewsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedReviewsRepository extends JpaRepository<LikedReviews, LikedReviewsId> {
    @Query("SELECT l.id.reviewId FROM LikedReviews l " +
            "JOIN Review r ON r.id.showId = :showId " +
            "WHERE l.id.userId = :userId ")
    List<Long> findReviewIdsLikedByUserAndShow(@Param("userId")Long userId, @Param("showId") Long showId);
}
