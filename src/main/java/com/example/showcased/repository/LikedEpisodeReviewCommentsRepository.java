package com.example.showcased.repository;

import com.example.showcased.entity.LikedEpisodeReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedEpisodeReviewCommentsRepository extends JpaRepository<LikedEpisodeReviewComment, Long> {
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    Optional<LikedEpisodeReviewComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
