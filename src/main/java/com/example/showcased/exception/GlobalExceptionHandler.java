package com.example.showcased.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for user not found, will
     * return the id of the user that wasn't found
     * along with a 404 status
     * @param ex User not found exception object
     * @return JSON object with error attribute and associated message
     */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> userNotFoundHandler(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for invalid login request
     * aka username/password were incorrect
     * along with a 401 status
     * @param ex Invalid login exception object
     * @return JSON object with error attribute and associated message
     */
    @ExceptionHandler(InvalidLoginException.class)
    ResponseEntity<ErrorResponse> invalidLoginHandler(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for register request with
     * existing username along with a 409 status
     * @param ex Username taken exception object
     * @return JSON object with error attribute and associated message
     */
    @ExceptionHandler(UsernameTakenException.class)
    ResponseEntity<ErrorResponse> usernameTakenHandler(UsernameTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user performs an action
     * they need to be logged in to take
     * @param ex Not logged in exception object
     * @return JSON object with error attribute and associate message
     */
    @ExceptionHandler(NotLoggedInException.class)
    ResponseEntity<ErrorResponse> notLoggedInHandler(NotLoggedInException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }
}
