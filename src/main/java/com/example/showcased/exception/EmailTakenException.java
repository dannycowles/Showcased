package com.example.showcased.exception;

/**
 * This exception is thrown when a user attempts to register
 * an account with an email that is already in use
 */
public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message) {
        super(message);
    }
}
