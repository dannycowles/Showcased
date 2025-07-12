package com.example.showcased.repository;

import com.example.showcased.entity.LikedEpisodeReviewComment;
import com.example.showcased.entity.LikedEpisodeReviewCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedEpisodeReviewCommentsRepository extends JpaRepository<LikedEpisodeReviewComment, LikedEpisodeReviewCommentId> {
}
