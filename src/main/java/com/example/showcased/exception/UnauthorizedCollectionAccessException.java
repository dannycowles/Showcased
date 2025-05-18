package com.example.showcased.exception;

/**
 * This exception is thrown when a user tries to modify a collection that doesn't
 * belong to them.
 */
public class UnauthorizedCollectionAccessException extends RuntimeException {
    public UnauthorizedCollectionAccessException(String message) {
        super(message);
    }
}
