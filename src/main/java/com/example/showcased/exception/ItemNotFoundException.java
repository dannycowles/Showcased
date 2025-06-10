package com.example.showcased.exception;

/**
 * This exception is thrown when the item requested, is not found.
 * This could be anything from a review, collection, etc.
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
