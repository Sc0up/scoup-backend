package com.postsquad.scoup.web.error.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private int statusCode;
    private List<String> errors;

    protected ErrorResponse(LocalDateTime timestamp, String message, int statusCode, List<String> errors) {
        this.timestamp = timestamp;
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;
    }

    @Builder
    public static ErrorResponse of(LocalDateTime timestamp, String message, int statusCode, List<String> errors) {
        timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        return new ErrorResponse(timestamp, message, statusCode, errors);
    }

    public static ErrorResponse of(HttpStatus status, String message, List<FieldError> errors) {
        return ErrorResponse.builder()
                            .statusCode(status.value())
                            .message(message)
                            .errors(errors.stream()
                                          .map(ErrorResponse::joiningFieldErrorAndMessage)
                                          .collect(Collectors.toList()))
                            .build();
    }

    private static String joiningFieldErrorAndMessage(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    public static ErrorResponse of(HttpStatus status, String message, Set<ConstraintViolation<?>> errors) {
        return ErrorResponse.builder()
                            .statusCode(status.value())
                            .message(message)
                            .errors(errors.stream()
                                          .map(constraintViolation -> constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                                          .collect(Collectors.toList()))
                            .build();
    }
}
