package com.example.showcased.exception;

/**
 * This exception is used when the user attempts to upload a file
 * that is not an image for their profile picture
 */
public class InvalidFileType extends RuntimeException {
    public InvalidFileType(String message) {
        super(message);
    }
}
