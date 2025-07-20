package com.example.showcased.repository;

import com.example.showcased.entity.LikedShowReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedShowReviewCommentsRepository extends JpaRepository <LikedShowReviewComment, Long> {
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    Optional<LikedShowReviewComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
