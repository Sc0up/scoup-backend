package com.postsquad.scoup.web.signin.exception;

public class AuthorizationFailedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Authorization failed";

    private final String description;

    public AuthorizationFailedException(String message) {
        super(DEFAULT_MESSAGE);
        this.description = message;
    }

    public AuthorizationFailedException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
        this.description = message;
    }

    public String description() {
        return description;
    }
}
