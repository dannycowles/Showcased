package com.example.showcased.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler for user not found, will
     * return the id of the user that wasn't found
     */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> userNotFoundHandler(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for invalid login request
     * aka username/password were incorrect
     */
    @ExceptionHandler(InvalidLoginException.class)
    ResponseEntity<ErrorResponse> invalidLoginHandler(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for register request with
     * existing username
     */
    @ExceptionHandler(UsernameTakenException.class)
    ResponseEntity<ErrorResponse> usernameTakenHandler(UsernameTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for register request with
     * existing email
     */
    @ExceptionHandler(EmailTakenException.class)
    ResponseEntity<ErrorResponse> alreadyOnListHandler(EmailTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user performs an action
     * they need to be logged in to take
     */
    @ExceptionHandler(NotLoggedInException.class)
    ResponseEntity<ErrorResponse> notLoggedInHandler(NotLoggedInException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user tries to add a show to their list
     * that is already on their list
     */
    @ExceptionHandler(AlreadyOnListException.class)
    ResponseEntity<ErrorResponse> alreadyOnListHandler(AlreadyOnListException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for validation errors, grabs the first one
     * and sends it back
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Retrieve the first validation error message
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

    /**
     * Exception handler for when a user tries to like something they already have
     */
    @ExceptionHandler(AlreadyLikedException.class)
    public ResponseEntity<ErrorResponse> alreadyLikedShowReviewHandler(AlreadyLikedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user tries to unlike something they haven't yet liked
     */
    @ExceptionHandler(HaventLikedException.class)
    public ResponseEntity<ErrorResponse> haventLikedShowReviewHandler(HaventLikedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user tries to add multiple
     * reviews to the same show
     */
    @ExceptionHandler(AlreadyReviewedShowException.class)
    public ResponseEntity<ErrorResponse> alreadyReviewedShowHandler(AlreadyReviewedShowException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when a user enters invalid or expired OTP code
     */
    @ExceptionHandler(OTPValidationException.class)
    public ResponseEntity<ErrorResponse> otpValidationHandler(OTPValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for invalid change password attempt
     */
    @ExceptionHandler(NotVerifiedException.class)
    public ResponseEntity<ErrorResponse> notVerifiedHandler(NotVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for invalid file type for profile picture upload
     */
    @ExceptionHandler(InvalidFileType.class)
    ResponseEntity<ErrorResponse> invalidFileTypeHandler(InvalidFileType ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for trying to follow yourself
     */
    @ExceptionHandler(FollowSelfException.class)
    ResponseEntity<ErrorResponse> followSelfHandler(FollowSelfException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for trying to add invalid character type to rankings
     */
    @ExceptionHandler(InvalidCharacterType.class)
    ResponseEntity<ErrorResponse> invalidCharacterTypeHandler(InvalidCharacterType ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for invalid page number for filter
     */
    @ExceptionHandler(InvalidPageException.class)
    ResponseEntity<ErrorResponse> invalidPageHandler(InvalidPageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for duplicate collection names
     */
    @ExceptionHandler(DuplicateCollectionNameException.class)
    ResponseEntity<ErrorResponse> duplicateCollectionNameHandler(DuplicateCollectionNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for trying to modify or delete an item that doesn't belong to them
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    ResponseEntity<ErrorResponse> unauthorizedAccessHandler(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for user collection not found
     */
    @ExceptionHandler(CollectionNotFoundException.class)
    ResponseEntity<ErrorResponse> collectionNotFoundHandler(CollectionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for trying to add a show to a collection that already has that show
     */
    @ExceptionHandler(AlreadyInCollectionException.class)
    ResponseEntity<ErrorResponse> alreadyInCollectionHandler(AlreadyInCollectionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler when some item being requested was not found
     */
    @ExceptionHandler(ItemNotFoundException.class)
    ResponseEntity<ErrorResponse> itemNotFoundHandler(ItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler when same characters is duplicated in dynamic ranking
     */
    @ExceptionHandler(InvalidDynamicException.class)
    ResponseEntity<ErrorResponse> invalidDynamicHandler(InvalidDynamicException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    /**
     * Exception handler for when the recaptcha form is invalid
     */
    @ExceptionHandler(RecaptchaInvalidException.class)
    ResponseEntity<ErrorResponse> recaptchaInvalidHandler(RecaptchaInvalidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }
}
