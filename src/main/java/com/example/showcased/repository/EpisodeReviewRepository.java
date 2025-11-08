package com.example.showcased.repository;

import com.example.showcased.dto.EpisodeReviewDto;
import com.example.showcased.dto.EpisodeReviewWithUserInfoDto;
import com.example.showcased.dto.ReviewDistributionDto;
import com.example.showcased.entity.EpisodeReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeReviewRepository extends JpaRepository<EpisodeReview, Long> {
    @Query(""" 
        SELECT new com.example.showcased.dto.EpisodeReviewWithUserInfoDto(
                r.id,
                u.displayName,
                u.profilePicture,
                r.userId,
                e.showId,
                e.showTitle,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.numComments,
                r.reviewDate,
                CASE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.reviewId = r.id AND lr.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                END,
                CASE WHEN r.userId = :userId THEN TRUE ELSE FALSE END,
                e.season,
                e.episode,
                e.episodeTitle
            )
            FROM EpisodeReview r
            JOIN User u ON r.userId = u.id
            JOIN EpisodeInfo e ON r.episodeId = e.id
            WHERE r.episodeId = :episodeId
        """)
    Page<EpisodeReviewWithUserInfoDto> findAllByEpisodeId(@Param("episodeId") Long episodeId, @Param("userId") Long userId, Pageable page);

    @Query(""" 
        SELECT new com.example.showcased.dto.EpisodeReviewWithUserInfoDto(
                r.id,
                u.displayName,
                u.profilePicture,
                r.userId,
                e.showId,
                e.showTitle,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.numComments,
                r.reviewDate,
                CASE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.reviewId = :reviewId AND lr.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                END,
                CASE WHEN r.userId = :userId THEN TRUE ELSE FALSE END,
                e.season,
                e.episode,
                e.episodeTitle
            )
            FROM EpisodeReview r
            JOIN User u ON r.userId = u.id
            JOIN EpisodeInfo e ON r.episodeId = e.id
            WHERE r.id = :reviewId
            ORDER BY r.reviewDate DESC
        """)
    EpisodeReviewWithUserInfoDto findById(@Param("reviewId") Long reviewId, @Param("userId") Long userId);

    @Query(""" 
        SELECT new com.example.showcased.dto.EpisodeReviewDto (
                r.id,
                e.showId,
                r.rating,
                e.showTitle,
                r.commentary,
                r.containsSpoilers,
                e.posterPath,
                r.reviewDate,
                r.numLikes,
                e.episodeTitle,
                e.season,
                e.episode,
                CASE
                    WHEN :loggedInUserId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.userId = :loggedInUserId AND lr.reviewId = r.id
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM EpisodeReview r
            JOIN EpisodeInfo e ON e.id = r.episodeId
            JOIN User u ON u.id = r.userId
            WHERE u.displayName = :username
        """)
    Page<EpisodeReviewDto> findByUsername(@Param("username") String username, @Param("loggedInUserId") Long loggedInUserId, Pageable pageable);

    @Query(value = """
        WITH RECURSIVE rating_values(rating) AS (
            SELECT 1
            UNION ALL
            SELECT rating + 1
            FROM rating_values
            WHERE rating < 10
        )
        SELECT rv.rating, COUNT(er.rating) as numReviews
        FROM rating_values rv
        LEFT JOIN episode_reviews er ON ROUND(er.rating, 0) = rv.rating AND er.episode_id = :episodeId
        GROUP BY rv.rating;
    """, nativeQuery = true)
    List<ReviewDistributionDto> getEpisodeReviewDistribution(@Param("episodeId") Long episodeId);

    void deleteByUserIdAndEpisodeId(Long userId, Long episodeId);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numLikes = r.numLikes + 1 WHERE r.id = :reviewId")
    void incrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numLikes = r.numLikes - 1 WHERE r.id = :reviewId")
    void decrementLikes(@Param("reviewId") Long reviewId);

    @Query("""
        SELECT new com.example.showcased.dto.EpisodeReviewWithUserInfoDto(
                r.id,
                u.displayName,
                u.profilePicture,
                r.userId,
                e.showId,
                e.showTitle,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.numComments,
                r.reviewDate,
                FALSE,
                TRUE,
                e.season,
                e.episode,
                e.episodeTitle
        )
        FROM EpisodeReview r
        JOIN EpisodeInfo e ON e.id = r.episodeId
        JOIN User u ON r.userId = u.id
        WHERE r.id = :id
""")
    EpisodeReviewWithUserInfoDto findByIdWithUserInfo(@Param("id") Long id);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numComments = r.numComments + 1 WHERE r.id = :reviewId")
    void incrementNumComments(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numComments = r.numComments - 1 WHERE r.id = :reviewId")
    void decrementNumComments(@Param("reviewId") Long reviewId);
}
