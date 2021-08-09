package com.postsquad.scoup.web.error.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
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

    public static ErrorResponse of(HttpStatus status, List<FieldError> errors) {
        return ErrorResponse.of(
                LocalDateTime.now(),
                status.getReasonPhrase(),
                status.value(),
                errors.stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .collect(Collectors.toList())
        );
    }
}