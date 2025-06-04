package com.example.showcased.repository;

import com.example.showcased.dto.ReviewWithUserInfoDto;
import com.example.showcased.entity.Review;
import com.example.showcased.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    @Query(""" 
        SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(
                r.reviewId,
                u.username,
                u.profilePicture,
                r.id.reviewerId,
                r.id.showId,
                r.showTitle,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedReviews lr
                            WHERE lr.id.reviewId = r.reviewId AND lr.id.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM Review r
            JOIN User u ON r.id.reviewerId = u.id
            WHERE r.id.showId = :showId
        """)
    List<ReviewWithUserInfoDto> findAllByShowId(@Param("showId") Long showId, @Param("userId") Long userId);

    @Query("""
        SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(
            r.reviewId,
            u.username,
            u.profilePicture,
            r.id.reviewerId,
            r.id.showId,
            r.showTitle,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.reviewDate,
            CASE
                WHEN EXISTS (
                    SELECT lr FROM LikedReviews lr
                    WHERE lr.id.reviewId = r.reviewId AND lr.id.userId = :id
                ) THEN TRUE ELSE FALSE
            END
        )
        FROM Review r
        JOIN User u ON r.id.reviewerId = u.id
        WHERE r.id.reviewerId = :id
        ORDER BY r.reviewDate DESC
    """)
    List<ReviewWithUserInfoDto> findByUserId(@Param("id") Long id);

    @Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId")
    Review findByReviewId(@Param("reviewId") Long reviewId);
}
