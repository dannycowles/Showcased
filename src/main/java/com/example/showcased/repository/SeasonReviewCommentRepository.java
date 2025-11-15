package com.example.showcased.repository;

import com.example.showcased.dto.ReviewCommentWithUserInfoDto;
import com.example.showcased.entity.SeasonReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeasonReviewCommentRepository extends JpaRepository<SeasonReviewComment, Long> {

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.displayName,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            FALSE,
            TRUE
        )
        FROM SeasonReviewComment c
        JOIN User u ON c.userId = u.id
        WHERE c.id = :commentId
""")
    ReviewCommentWithUserInfoDto findByIdWithUserInfo(@Param("commentId") Long commentId);

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.displayName,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            CASE
                WHEN :userId IS NULL THEN FALSE
                WHEN EXISTS (
                    SELECT lc FROM LikedSeasonReviewComment lc
                    WHERE lc.commentId = c.id AND lc.userId = :userId
                ) THEN TRUE
                ELSE FALSE
            END,
            CASE WHEN c.userId = :userId THEN TRUE ELSE FALSE END
        )
        FROM SeasonReviewComment c
        JOIN User u ON u.id = c.userId
        WHERE c.reviewId = :reviewId
""")
    Page<ReviewCommentWithUserInfoDto> findAllByReviewId(@Param("reviewId") Long reviewId, @Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.displayName,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            CASE
                WHEN :userId IS NULL THEN FALSE
                WHEN EXISTS (
                    SELECT lc FROM LikedSeasonReviewComment lc
                    WHERE lc.commentId = c.id AND lc.userId = :userId
                ) THEN TRUE
                ELSE FALSE
            END,
            CASE WHEN c.userId = :userId THEN TRUE ELSE FALSE END
        )
        FROM SeasonReviewComment c
        JOIN User u ON u.id = c.userId
        WHERE c.id = :commentId
""")
    ReviewCommentWithUserInfoDto findById(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE SeasonReviewComment c SET c.numLikes = c.numLikes + 1 WHERE c.id = :commentId")
    void incrementNumLikes(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE SeasonReviewComment c SET c.numLikes = c.numLikes - 1 WHERE c.id = :commentId")
    void decrementNumLikes(@Param("commentId") Long commentId);
}
