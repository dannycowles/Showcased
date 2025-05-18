package com.example.showcased.exception;

public class AlreadyInCollectionException extends RuntimeException {
    public AlreadyInCollectionException(String message) {
        super(message);
    }
}
