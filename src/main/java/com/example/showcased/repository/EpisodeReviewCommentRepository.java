package com.example.showcased.repository;

import com.example.showcased.dto.ReviewCommentWithUserInfoDto;
import com.example.showcased.entity.EpisodeReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeReviewCommentRepository extends JpaRepository<EpisodeReviewComment, Long> {

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
        FROM EpisodeReviewComment c
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
            FALSE
        )
        FROM EpisodeReview r
        JOIN EpisodeReviewComment c ON r.id = c.reviewId
        JOIN User u ON c.userId = u.id
        WHERE r.id = :reviewId
        ORDER BY c.createdAt ASC
""")
    List<ReviewCommentWithUserInfoDto> findAllByReviewId(@Param("reviewId") Long reviewId, @Param("userId") Long userId);
    // TODO: after doing liked episode review comments come back and use case instead of false
}
