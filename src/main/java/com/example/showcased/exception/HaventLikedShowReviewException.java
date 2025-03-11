package com.example.showcased.exception;

/**
 * This exception is thrown when a user attempts to unlike a show review
 * that they haven't liked in the first place
 */
public class HaventLikedShowReviewException extends RuntimeException {
    public HaventLikedShowReviewException(String message) {
        super(message);
    }
}
