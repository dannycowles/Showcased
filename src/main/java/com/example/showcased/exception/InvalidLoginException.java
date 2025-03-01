package com.example.showcased.exception;

/**
 * This exception class is used when the user login
 * credentials are invalid
 */
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("Invalid login credentials");
    }
}
