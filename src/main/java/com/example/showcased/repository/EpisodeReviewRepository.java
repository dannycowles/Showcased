package com.example.showcased.repository;

import com.example.showcased.entity.EpisodeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeReviewRepository extends JpaRepository<EpisodeReview, Long> {
}
