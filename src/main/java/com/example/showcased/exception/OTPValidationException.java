package com.example.showcased.exception;

/**
 * This exception class is used during the OTP validation process
 * will provide error message depending on result
 */
public class OTPValidationException extends RuntimeException {
    public OTPValidationException(String message) {
        super(message);
    }
}
