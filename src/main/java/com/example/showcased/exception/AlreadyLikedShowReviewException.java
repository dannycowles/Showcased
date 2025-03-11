package com.example.showcased.exception;

/**
 * This exception is thrown when the user attempts to like a show review
 * that they already have
 */
public class AlreadyLikedShowReviewException extends RuntimeException {
    public AlreadyLikedShowReviewException(String message) {
        super(message);
    }
}
