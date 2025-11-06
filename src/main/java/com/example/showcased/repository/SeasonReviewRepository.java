package com.example.showcased.repository;

import com.example.showcased.dto.ReviewDistributionDto;
import com.example.showcased.dto.SeasonReviewDto;
import com.example.showcased.dto.SeasonReviewWithUserInfoDto;
import com.example.showcased.entity.SeasonReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeasonReviewRepository extends JpaRepository<SeasonReview, Long> {
    void deleteByUserIdAndSeasonId(Long id, Long seasonId);

    @Query("""
        SELECT new com.example.showcased.dto.SeasonReviewWithUserInfoDto(
            r.id,
            u.displayName,
            u.profilePicture,
            u.id,
            r.showId,
            s.showTitle,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.numComments,
            r.reviewDate,
            FALSE,
            TRUE,
            s.season
        )
        FROM SeasonReview r
        JOIN SeasonInfo s ON r.seasonId = s.id
        JOIN User u ON r.userId = u.id
        WHERE r.id = :id
""")
    SeasonReviewWithUserInfoDto findByIdWithUserInfo(@Param("id") Long id);

    @Query("""
        SELECT new com.example.showcased.dto.SeasonReviewWithUserInfoDto(
            r.id,
            u.displayName,
            u.profilePicture,
            u.id,
            r.showId,
            s.showTitle,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.numComments,
            r.reviewDate,
            CASE
                WHEN EXISTS (
                    SELECT lr FROM LikedShowReview lr
                    WHERE lr.reviewId = r.id AND lr.userId = :userId
                ) THEN TRUE ELSE FALSE
            END,
            CASE WHEN r.userId = :userId THEN TRUE ELSE FALSE END,
            s.season
        )
        FROM SeasonReview r
        JOIN SeasonInfo s ON r.seasonId = s.id
        JOIN User u ON r.userId = u.id
        WHERE r.seasonId = :seasonId
""")
    Page<SeasonReviewWithUserInfoDto> findAllBySeasonId(@Param("seasonId") Long seasonId, @Param("userId") Long userId, Pageable modifiedPage);

    @Query("""
        SELECT new com.example.showcased.dto.SeasonReviewWithUserInfoDto(
            r.id,
            u.displayName,
            u.profilePicture,
            u.id,
            r.showId,
            s.showTitle,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.numComments,
            r.reviewDate,
            CASE
                WHEN EXISTS (
                    SELECT lr FROM LikedSeasonReview lr
                    WHERE lr.reviewId = r.id AND lr.userId = :userId
                ) THEN TRUE ELSE FALSE
            END,
            CASE WHEN r.userId = :userId THEN TRUE ELSE FALSE END,
            s.season
        )
        FROM SeasonReview r
        JOIN SeasonInfo s ON r.seasonId = s.id
        JOIN User u ON r.userId = u.id
        WHERE r.id = :reviewId
""")
    SeasonReviewWithUserInfoDto findById(@Param("reviewId") Long reviewId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE SeasonReview r SET r.numLikes = r.numLikes + 1 WHERE r.id = :reviewId")
    void incrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE SeasonReview r SET r.numLikes = r.numLikes - 1 WHERE r.id = :reviewId")
    void decrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE SeasonReview r SET r.numComments = r.numComments + 1 WHERE r.id = :reviewId")
    void incrementNumComments(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE SeasonReview r SET r.numComments = r.numComments - 1 WHERE r.id = :reviewId")
    void decrementNumComments(@Param("reviewId") Long reviewId);

    @Query("""
        SELECT new com.example.showcased.dto.SeasonReviewDto(
            r.id,
            r.showId,
            r.rating,
            si.showTitle,
            r.commentary,
            r.containsSpoilers,
            si.posterPath,
            r.reviewDate,
            r.numLikes,
            CASE
                WHEN :loggedInUserId IS NULL THEN FALSE
                WHEN EXISTS (
                    SELECT lr FROM LikedSeasonReview lr
                    WHERE lr.reviewId = r.id AND lr.userId = :loggedInUserId
                ) THEN TRUE
                ELSE FALSE
            END,
            si.season
        )
        FROM SeasonReview r
        JOIN SeasonInfo si ON r.seasonId = si.id
        JOIN User u ON u.id = r.userId
        WHERE u.displayName = :username
""")
    Page<SeasonReviewDto> findByUsername(@Param("username") String username, @Param("loggedInUserId") Long loggedInUserId, Pageable pageable);

    @Query(value = """
        WITH RECURSIVE rating_values(rating) AS (
            SELECT 1
            UNION ALL
            SELECT rating + 1
            FROM rating_values
            WHERE rating < 10
        )
        SELECT rv.rating, COUNT(sr.rating) as numReviews
        FROM rating_values rv
        LEFT JOIN season_reviews sr ON ROUND(sr.rating, 0) = rv.rating AND sr.season_id = :seasonId
        GROUP BY rv.rating;
    """, nativeQuery = true)
    List<ReviewDistributionDto> getSeasonReviewDistribution (@Param("seasonId") Long seasonId);
}
