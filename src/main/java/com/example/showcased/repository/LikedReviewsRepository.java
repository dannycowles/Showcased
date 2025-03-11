package com.example.showcased.repository;

import com.example.showcased.entity.LikedReviews;
import com.example.showcased.entity.LikedReviewsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedReviewsRepository extends JpaRepository<LikedReviews, LikedReviewsId> {
}
