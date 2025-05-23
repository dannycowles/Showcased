package com.example.showcased.exception;

/**
 * This exception is thrown when a user attempts to unlike an item they haven't yet liked
 */
public class HaventLikedException extends RuntimeException {
    public HaventLikedException(String message) {
        super(message);
    }
}
