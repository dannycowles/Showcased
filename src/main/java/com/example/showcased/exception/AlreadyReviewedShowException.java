package com.example.showcased.exception;

/**
 * This exception is thrown when the user attempts to add more than
 * one review to a particular show
 */
public class AlreadyReviewedShowException extends RuntimeException {
    public AlreadyReviewedShowException(String message) {
        super(message);
    }
}
