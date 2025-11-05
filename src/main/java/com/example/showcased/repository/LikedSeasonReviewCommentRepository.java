package com.example.showcased.repository;

import com.example.showcased.entity.LikedSeasonReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedSeasonReviewCommentRepository extends JpaRepository<LikedSeasonReviewComment, Long> {
    boolean existsByUserIdAndCommentId(Long id, Long commentId);

    Optional<LikedSeasonReviewComment> findByUserIdAndCommentId(Long id, Long commentId);
}
