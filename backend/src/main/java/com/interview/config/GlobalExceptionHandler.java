package com.interview.config;

import com.interview.dto.ErrorResponse;
import com.interview.dto.ErrorResponse.ValidationError;
import com.interview.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // Error codes
    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";
    private static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";

    // Error messages
    private static final String VALIDATION_ERROR_MESSAGE = "Request validation failed";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred";

    /**
     * Handle all BusinessException types (CustomerNotFoundException, CustomerAlreadyExistsException, etc.)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
        BusinessException ex, HttpServletRequest request) {

        log.warn("Business exception [{}]: {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getRequestURI(),
            ex.getStatus().value()
        );

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    /**
     * Handle validation errors from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation failed: {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ValidationError(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ))
            .toList();

        ErrorResponse errorResponse = ErrorResponse.withValidationErrors(
            VALIDATION_ERROR_CODE,
            VALIDATION_ERROR_MESSAGE,
            request.getRequestURI(),
            HttpStatus.BAD_REQUEST.value(),
            validationErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle all unhandled exceptions (both checked and unchecked)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex, HttpServletRequest request) {

        log.error("Unexpected exception", ex);

        ErrorResponse errorResponse = ErrorResponse.of(
            INTERNAL_SERVER_ERROR_CODE,
            INTERNAL_SERVER_ERROR_MESSAGE,
            request.getRequestURI(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}