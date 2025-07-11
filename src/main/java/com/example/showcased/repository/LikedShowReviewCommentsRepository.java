package com.example.showcased.repository;

import com.example.showcased.entity.LikedShowReviewComment;
import com.example.showcased.entity.LikedShowReviewCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedShowReviewCommentsRepository extends JpaRepository <LikedShowReviewComment, LikedShowReviewCommentId> {
}
