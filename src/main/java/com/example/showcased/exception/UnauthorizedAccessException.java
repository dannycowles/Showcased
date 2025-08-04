package com.example.showcased.exception;

/**
 * This exception is thrown when a user tries to modify an item that doesn't belong to them
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
