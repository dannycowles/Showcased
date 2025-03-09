package com.example.showcased.exception;

/**
 * This exception is thrown when the user attempts to add
 * a show to their watchlist, currently watching list, or ranking list
 * and that show is already on their list, also applies to episodes on ranking list
 */
public class AlreadyOnListException extends RuntimeException {
    public AlreadyOnListException(String message) {
        super(message);
    }
}
