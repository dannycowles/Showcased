package com.example.showcased.repository;

import com.example.showcased.dto.ReviewWithUserInfoDto;
import com.example.showcased.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(r.id, u.username, r.reviewerId, r.showId, r.rating, r.commentary, r.containsSpoilers, r.numLikes, r.reviewDate) " +
            "FROM Review r JOIN User u ON r.reviewerId = u.id WHERE r.showId = ?1")
    List<ReviewWithUserInfoDto> findAllByShowId(Long showId);

    @Query("SELECT new com.example.showcased.dto.ReviewWithUserInfoDto(r.id, u.username, r.reviewerId, r.showId, r.rating, r.commentary, r.containsSpoilers, r.numLikes, r.reviewDate) " +
            "FROM Review r JOIN User u ON r.reviewerId = u.id WHERE r.reviewerId = ?1 ORDER BY r.reviewDate DESC")
    List<ReviewWithUserInfoDto> findByUserId(Long id);
}
