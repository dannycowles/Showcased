package com.example.showcased.controller;

import com.example.showcased.dto.ReviewWithUserInfoDto;
import com.example.showcased.entity.Review;
import com.example.showcased.exception.NotLoggedInException;
import com.example.showcased.service.ReviewService;
import jakarta.servlet.http.HttpSession;
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
    public List<ReviewWithUserInfoDto> getReviewsByShow(@PathVariable Long showId) {
        return reviewService.getReviewsByShow(showId);
    }

    @PostMapping()
    public ResponseEntity<?> addReviewToShow(@RequestBody Review review, HttpSession session) {
        // If the user is not logged in they shouldn't be able to write a review so we throw an exception
        System.out.println(session.getAttribute("user"));
        if (session.getAttribute("user") == null) {
            throw new NotLoggedInException();
        }

        reviewService.addReviewToShow(review);
        return ResponseEntity.ok().build();
    }
}
