package com.example.showcased.exception;

/**
 * This exception class is used when a user is not found
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Could not find user with id " + id);
    }
    public UserNotFoundException(String username) { super("Could not find user with name " + username); }
}
