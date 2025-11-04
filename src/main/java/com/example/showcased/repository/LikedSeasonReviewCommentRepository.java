package com.example.showcased.repository;

import com.example.showcased.entity.LikedSeasonReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedSeasonReviewCommentRepository extends JpaRepository<LikedSeasonReviewComment, Long> {
}
