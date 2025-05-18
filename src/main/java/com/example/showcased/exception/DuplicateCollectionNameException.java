package com.example.showcased.exception;

/**
 * This exception is thrown when the user attempts to create a new collection
 * with the same name as one they already have
 */
public class DuplicateCollectionNameException extends RuntimeException {
    public DuplicateCollectionNameException(String message) {
        super(message);
    }
}
