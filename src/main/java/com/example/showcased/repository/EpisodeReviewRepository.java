package com.example.showcased.repository;

import com.example.showcased.dto.EpisodeReviewDto;
import com.example.showcased.dto.EpisodeReviewWithUserInfoDto;
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
                u.username,
                u.profilePicture,
                r.userId,
                e.showId,
                e.season,
                e.episode,
                e.showTitle,
                e.episodeTitle,
                r.rating,
                r.commentary,
                r.containsSpoilers,
                r.numLikes,
                r.numComments,
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.reviewId = r.id AND lr.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
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
                u.username,
                u.profilePicture,
                r.userId,
                e.showId,
                e.season,
                e.episode,
                e.showTitle,
                e.episodeTitle,
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
                 END
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
                e.showId,
                r.rating,
                e.showTitle,
                r.commentary,
                r.containsSpoilers,
                e.posterPath,
                r.reviewDate,
                e.episodeTitle,
                e.season,
                e.episode
            )
            FROM EpisodeReview r
            JOIN EpisodeInfo e ON e.id = r.episodeId
            JOIN User u ON u.id = r.userId
            WHERE r.userId = :userId
        """)
    List<EpisodeReviewDto> findByUserId(@Param("userId") Long userId);

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
            u.username,
            u.profilePicture,
            r.userId,
            e.showId,
            e.season,
            e.episode,
            e.showTitle,
            e.episodeTitle,
            r.rating,
            r.commentary,
            r.containsSpoilers,
            r.numLikes,
            r.numComments,
            r.reviewDate,
            FALSE
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
}
