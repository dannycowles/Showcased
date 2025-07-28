package com.example.showcased.repository;

import com.example.showcased.dto.ReviewCommentWithUserInfoDto;
import com.example.showcased.entity.ShowReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowReviewCommentRepository extends JpaRepository<ShowReviewComment, Long> {

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.username,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            FALSE
        )
        FROM ShowReviewComment c
        JOIN User u ON c.userId = u.id
        WHERE c.id = :commentId
""")
    ReviewCommentWithUserInfoDto findByIdWithUserInfo(@Param("commentId") Long commentId);

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.username,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            CASE
                WHEN :userId IS NULL THEN FALSE
                WHEN EXISTS (
                    SELECT lr FROM LikedShowReviewComment lr
                    WHERE lr.commentId = c.id AND lr.userId = :userId
                ) THEN TRUE
                ELSE FALSE
           END
        )
        FROM ShowReview r
        JOIN ShowReviewComment c ON r.id = c.reviewId
        JOIN User u ON c.userId = u.id
        WHERE r.id = :reviewId
        ORDER BY c.createdAt ASC
""")
    Page<ReviewCommentWithUserInfoDto> findAllByReviewId(@Param("reviewId") Long reviewId, @Param("userId") Long userId, Pageable page);

    @Query("""
        SELECT new com.example.showcased.dto.ReviewCommentWithUserInfoDto(
            c.id,
            c.reviewId,
            c.userId,
            u.username,
            u.profilePicture,
            c.commentText,
            c.numLikes,
            c.createdAt,
            CASE
                WHEN EXISTS (
                    SELECT lr FROM LikedShowReviewComment lr
                    WHERE lr.commentId = :commentId AND lr.userId = :userId
                ) THEN TRUE
                ELSE FALSE
           END
        )
        FROM ShowReview r
        JOIN ShowReviewComment c ON r.id = c.reviewId
        JOIN User u ON c.userId = u.id
        WHERE c.id = :commentId
        ORDER BY c.createdAt ASC
""")
    ReviewCommentWithUserInfoDto findById(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ShowReviewComment r SET r.numLikes = r.numLikes + 1 WHERE r.id = :commentId")
    void incrementNumLikes(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE ShowReviewComment r SET r.numLikes = r.numLikes - 1 WHERE r.id = :commentId")
    void decrementNumLikes(@Param("commentId") Long commentId);
}
