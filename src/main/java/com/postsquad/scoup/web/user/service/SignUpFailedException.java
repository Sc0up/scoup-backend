package com.postsquad.scoup.web.user.service;

public class SignUpFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Sign up failed";
    private String description;

    public SignUpFailedException(String message) {
        super(DEFAULT_MESSAGE);
        description = message;
    }

    public SignUpFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public SignUpFailedException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        description = message;
    }

    public String description() {
        return description;
    }
}
