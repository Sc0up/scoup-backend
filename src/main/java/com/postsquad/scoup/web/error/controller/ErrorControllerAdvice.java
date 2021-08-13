package com.postsquad.scoup.web.error.controller;

import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        String message = "method argument not valid";
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message, methodArgumentNotValidException.getFieldErrors());
    }
}
