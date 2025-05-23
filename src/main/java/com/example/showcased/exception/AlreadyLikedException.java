package com.example.showcased.exception;

/**
 * This exception is thrown when the user attempts to like an item that they have already liked
 */
public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(String message) {
        super(message);
    }
}
