package com.example.showcased.exception;

/**
 * This exception is thrown when a user attempts to retrieve a page number
 * of a resource that is invalid
 */
public class InvalidPageException extends RuntimeException {
    public InvalidPageException(String message) {
        super(message);
    }
}
