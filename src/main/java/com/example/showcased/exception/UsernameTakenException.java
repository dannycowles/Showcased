package com.example.showcased.exception;

/**
 * This exception class is used when the username
 * requested when creating an account is already taken
 */
public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String message) {
        super(message);
    }
}
