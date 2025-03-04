package com.example.showcased.service;

import com.example.showcased.entity.Review;
import com.example.showcased.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByShow(Long showId) {
        return reviewRepository.findAllByShowId(showId);
    }

    public void addReviewToShow(Review review) {
        reviewRepository.save(review);
    }
}
