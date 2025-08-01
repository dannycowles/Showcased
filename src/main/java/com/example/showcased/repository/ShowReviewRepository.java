package com.example.showcased.repository;

import com.example.showcased.dto.ShowReviewDto;
import com.example.showcased.dto.ShowReviewWithUserInfoDto;
import com.example.showcased.entity.ShowReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
                r.numComments,
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedShowReview lr
                            WHERE lr.reviewId = r.id AND lr.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM ShowReview r
            JOIN User u ON r.userId = u.id
            JOIN ShowInfo s ON r.showId = s.showId
            WHERE r.showId = :showId
        """)
    Page<ShowReviewWithUserInfoDto> findAllByShowId(@Param("showId") Long showId, @Param("userId") Long userId, Pageable page);

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
                r.numComments,
                r.reviewDate,
                CASE
                    WHEN EXISTS (
                            SELECT lr FROM LikedShowReview lr
                            WHERE lr.reviewId = :reviewId AND lr.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM ShowReview r
            JOIN User u ON r.userId = u.id
            JOIN ShowInfo s ON r.showId = s.showId
            WHERE r.id = :reviewId
        """)
    ShowReviewWithUserInfoDto findById(@Param("reviewId") Long reviewId, @Param("userId") Long userId);

    @Query("""
        SELECT new com.example.showcased.dto.ShowReviewDto (
            s.showId,
            r.rating,
            s.title,
            r.commentary,
            r.containsSpoilers,
            s.posterPath,
            r.reviewDate
        )
        FROM ShowReview r
        JOIN User u ON r.userId = u.id
        JOIN ShowInfo s ON r.showId = s.showId
        WHERE r.userId = :userId
    """)
    List<ShowReviewDto> findByUserId(@Param("userId") Long userId);

    void deleteByUserIdAndShowId(Long userId, Long showId);

    @Modifying
    @Query("UPDATE ShowReview r SET r.numLikes = r.numLikes + 1 WHERE r.id = :reviewId")
    void incrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE ShowReview r SET r.numLikes = r.numLikes - 1 WHERE r.id = :reviewId")
    void decrementLikes(@Param("reviewId") Long reviewId);

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
            r.numComments,
            r.reviewDate,
            FALSE
        )
        FROM ShowReview r
        JOIN User u ON r.userId = u.id
        JOIN ShowInfo s ON r.showId = s.showId
        WHERE r.id = :id
""")
    ShowReviewWithUserInfoDto findByIdWithUserInfo(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ShowReview r SET r.numComments = r.numComments + 1 WHERE r.id = :reviewId")
    void incrementNumComments(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE ShowReview r SET r.numComments = r.numComments - 1 WHERE r.id = :reviewId")
    void decrementNumComments(@Param("reviewId") Long reviewId);
}
