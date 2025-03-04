package com.example.showcased.controller;

import com.example.showcased.entity.Review;
import com.example.showcased.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{showId}")
    public List<Review> getReviewsByShow(@PathVariable Long showId) {
        return reviewService.getReviewsByShow(showId);
    }

    @PostMapping()
    public ResponseEntity<?> addReviewToShow(@RequestBody Review review) {
        reviewService.addReviewToShow(review);
        return ResponseEntity.ok().build();
    }
}
