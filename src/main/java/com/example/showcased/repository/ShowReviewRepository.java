package com.example.showcased.repository;

import com.example.showcased.dto.ShowReviewWithUserInfoDto;
import com.example.showcased.entity.ShowReview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowReviewRepository extends JpaRepository<ShowReview, Long> {

    @Query(""" 
        SELECT new com.example.showcased.dto.ShowReviewWithUserInfoDto(
                r.id,
                u.username,
                u.profilePicture,
                r.userId,
                r.showId,
                s.title,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedShowReviews lr
                            WHERE lr.id.reviewId = r.id AND lr.id.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM ShowReview r
            JOIN User u ON r.userId = u.id
            JOIN ShowInfo s ON r.showId = s.showId
            WHERE r.showId = :showId
        """)
    List<ShowReviewWithUserInfoDto> findAllByShowId(@Param("showId") Long showId, @Param("userId") Long userId);

    @Query("""
        SELECT new com.example.showcased.dto.ShowReviewWithUserInfoDto(
            r.id,
            u.username,
            u.profilePicture,
            r.userId,
            r.showId,
            s.title,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.reviewDate,
            CASE
                WHEN EXISTS (
                    SELECT lr FROM LikedShowReviews lr
                    WHERE lr.id.reviewId = r.id AND lr.id.userId = :id
                ) THEN TRUE ELSE FALSE
            END
        )
        FROM ShowReview r
        JOIN User u ON r.userId = u.id
        JOIN ShowInfo s ON r.showId = s.showId
        WHERE r.userId = :id
        ORDER BY r.reviewDate DESC
    """)
    List<ShowReviewWithUserInfoDto> findByUserId(@Param("id") Long id);

    @Query("SELECT r FROM ShowReview r WHERE r.id = :reviewId")
    ShowReview findByReviewId(@Param("reviewId") Long reviewId);
    boolean existsByUserIdAndShowId(Long userId, Long showId);

    @Transactional
    void deleteByUserIdAndShowId(Long userId, Long showId);
}
