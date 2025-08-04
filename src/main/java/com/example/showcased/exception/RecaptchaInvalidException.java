package com.example.showcased.exception;

public class RecaptchaInvalidException extends RuntimeException {
    public RecaptchaInvalidException(String message) {
        super(message);
    }
}
