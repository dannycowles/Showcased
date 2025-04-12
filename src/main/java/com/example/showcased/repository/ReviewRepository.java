package com.example.showcased.repository;

import com.example.showcased.dto.ReviewWithUserInfoDto;
import com.example.showcased.entity.Review;
import com.example.showcased.entity.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    @Query("SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(r.reviewId, u.username, u.profilePicture, r.id.reviewerId, r.id.showId, r.showTitle, r.rating, r.commentary, r.containsSpoilers, r.numLikes, r.reviewDate) " +
            "FROM Review r JOIN User u ON r.id.reviewerId = u.id WHERE r.id.showId = ?1")
    List<ReviewWithUserInfoDto> findAllByShowId(Long showId);

    @Query("SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(r.reviewId, u.username, u.profilePicture, r.id.reviewerId, r.id.showId, r.showTitle, r.rating, r.commentary, r.containsSpoilers, r.numLikes, r.reviewDate) " +
            "FROM Review r JOIN User u ON r.id.reviewerId = u.id WHERE r.id.reviewerId = ?1 ORDER BY r.reviewDate DESC")
    List<ReviewWithUserInfoDto> findByUserId(Long id);

    @Query("SELECT r FROM Review r WHERE r.reviewId = ?1")
    Review findByReviewId(Long reviewId);
}
