package com.org.Rewards.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalRestControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalRestControllerAdvice.class);

    /**
     * Handles CustomerNotFoundException and returns a NOT_FOUND response.
     *
     * @param ex      The CustomerNotFoundException that was thrown.
     * @param request The WebRequest object.
     * @return ResponseEntity containing an ErrorResponse and NOT_FOUND status.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
        logger.error("CustomerNotFoundException occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles InvalidTransactionException and returns a BAD_REQUEST response.
     *
     * @param ex      The InvalidTransactionException that was thrown.
     * @param request The WebRequest object.
     * @return ResponseEntity containing an ErrorResponse and BAD_REQUEST status.
     */
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransactionException(InvalidTransactionException ex, WebRequest request) {
        logger.error("InvalidTransactionException occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles RuntimeException and returns an INTERNAL_SERVER_ERROR response.
     *
     * @param ex      The RuntimeException that was thrown.
     * @param request The WebRequest object.
     * @return ResponseEntity containing an ErrorResponse and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("RuntimeException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred: " + ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}