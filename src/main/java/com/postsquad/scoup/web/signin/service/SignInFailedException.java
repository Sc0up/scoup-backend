package com.postsquad.scoup.web.signin.service;

public class SignInFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Sign in failed";

    private String description;

    public SignInFailedException(String message) {
        super(DEFAULT_MESSAGE);
        description = message;
    }

    public SignInFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        description = cause.getMessage();
    }

    public SignInFailedException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        description = message;
    }

    public String description() {
        return description;
    }
}
