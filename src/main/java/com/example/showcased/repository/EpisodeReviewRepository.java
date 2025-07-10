package com.example.showcased.repository;

import com.example.showcased.dto.EpisodeReviewWithUserInfoDto;
import com.example.showcased.entity.EpisodeReview;
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
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.id.reviewId = r.id AND lr.id.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM EpisodeReview r
            JOIN User u ON r.userId = u.id
            JOIN EpisodeInfo e ON r.episodeId = e.id
            WHERE r.episodeId = :episodeId
            ORDER BY r.reviewDate DESC
        """)
    List<EpisodeReviewWithUserInfoDto> findAllByEpisodeId(@Param("episodeId") Long episodeId, @Param("userId") Long userId);

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
                r.reviewDate,
                CASE
                    WHEN :userId IS NULL THEN FALSE
                    WHEN EXISTS (
                            SELECT lr FROM LikedEpisodeReview lr
                            WHERE lr.id.reviewId = r.id AND lr.id.userId = :userId
                    ) THEN TRUE
                    ELSE FALSE
                 END
            )
            FROM EpisodeReview r
            JOIN EpisodeInfo e ON e.id = r.episodeId
            JOIN User u ON u.id = r.userId
            WHERE r.userId = :userId
            ORDER BY r.reviewDate DESC
        """)
    List<EpisodeReviewWithUserInfoDto> findByUserId(@Param("userId") Long userId);

    void deleteByUserIdAndEpisodeId(Long userId, Long episodeId);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numLikes = r.numLikes + 1 WHERE r.id = :reviewId")
    void incrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE EpisodeReview r SET r.numLikes = r.numLikes - 1 WHERE r.id = :reviewId")
    void decrementLikes(@Param("reviewId") Long reviewId);
}
