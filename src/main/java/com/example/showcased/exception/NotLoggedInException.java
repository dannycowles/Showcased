package com.example.showcased.exception;

/**
 * This exception class is used for actions that a user has to be logged in to take,
 * for example writing a review, adding shows to their watchlist or ranking lists,
 * accessing their profile page
 */
public class NotLoggedInException extends RuntimeException{
    public NotLoggedInException() {
        super("User is not logged in.");
    }
}
