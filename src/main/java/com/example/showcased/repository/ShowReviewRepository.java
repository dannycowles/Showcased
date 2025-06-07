package com.example.showcased.repository;

import com.example.showcased.dto.ShowReviewWithUserInfoDto;
import com.example.showcased.entity.ShowReview;
import com.example.showcased.entity.ShowReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowReviewRepository extends JpaRepository<ShowReview, ShowReviewId> {

    @Query(""" 
        SELECT new com.example.showcased.dto.ShowReviewWithUserInfoDto(
                r.id,
                u.username,
                u.profilePicture,
                r.key.userId,
                r.key.showId,
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
            JOIN User u ON r.key.userId = u.id
            JOIN ShowInfo s ON r.key.showId = s.showId
            WHERE r.key.showId = :showId
        """)
    List<ShowReviewWithUserInfoDto> findAllByShowId(@Param("showId") Long showId, @Param("userId") Long userId);

    @Query("""
        SELECT new com.example.showcased.dto.ShowReviewWithUserInfoDto(
            r.id,
            u.username,
            u.profilePicture,
            r.key.userId,
            r.key.showId,
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
        JOIN User u ON r.key.userId = u.id
        JOIN ShowInfo s ON r.key.showId = s.showId
        WHERE r.key.userId = :id
        ORDER BY r.reviewDate DESC
    """)
    List<ShowReviewWithUserInfoDto> findByUserId(@Param("id") Long id);

    @Query("SELECT r FROM ShowReview r WHERE r.id = :reviewId")
    ShowReview findByReviewId(@Param("reviewId") Long reviewId);
}
