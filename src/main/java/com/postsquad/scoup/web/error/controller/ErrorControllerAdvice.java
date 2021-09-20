package com.postsquad.scoup.web.error.controller;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.signin.exception.AuthorizationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = "Method argument not valid.";
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message, methodArgumentNotValidException.getFieldErrors());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandler(ConstraintViolationException constraintViolationException) {
        String message = "Method argument not valid.";
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message, constraintViolationException.getConstraintViolations());
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authenticationFailedExceptionHandler(AuthorizationFailedException authorizationFailedException) {
        return ErrorResponse.of(HttpStatus.UNAUTHORIZED, authorizationFailedException.getMessage(), authorizationFailedException.description());
    }
}
