package com.example.showcased.exception;

/**
 * This exception is thrown when a user attempts to follow themselves
 */
public class FollowSelfException extends RuntimeException {
    public FollowSelfException(String message) {
        super(message);
    }
}
