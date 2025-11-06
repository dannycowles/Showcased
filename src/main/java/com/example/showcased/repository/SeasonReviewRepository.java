package com.example.showcased.repository;

import com.example.showcased.dto.SeasonReviewWithUserInfoDto;
import com.example.showcased.entity.SeasonReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
