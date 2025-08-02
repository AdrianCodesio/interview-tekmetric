package com.interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String error,
    String message,
    String path,
    Integer status,
    LocalDateTime timestamp,
    List<ValidationError> validationErrors
) {

    public static ErrorResponse of(String error, String message, String path, Integer status) {
        return new ErrorResponse(error, message, path, status, LocalDateTime.now(), null);
    }

    public static ErrorResponse withValidationErrors(String error, String message, String path,
        Integer status, List<ValidationError> validationErrors) {
        return new ErrorResponse(error, message, path, status, LocalDateTime.now(), validationErrors);
    }

    public record ValidationError(
        String field,
        Object rejectedValue,
        String message
    ) {}
}