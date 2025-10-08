package com.example.showcased.exception;

/**
 * Thrown when the username provided upon register contains spaces or special characters
 */
public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
