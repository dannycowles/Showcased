package com.example.showcased.exception;

/**
 * This exception class is used when the user attempts to change a password
 * when they have not been verified
 */
public class NotVerifiedException extends RuntimeException {
    public NotVerifiedException(String message) {
        super(message);
    }
}
