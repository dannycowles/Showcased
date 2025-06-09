package com.example.showcased.repository;

import com.example.showcased.entity.LikedEpisodeReview;
import com.example.showcased.entity.LikedEpisodeReviewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedEpisodeReviewsRepository extends JpaRepository<LikedEpisodeReview, LikedEpisodeReviewId> {
}
