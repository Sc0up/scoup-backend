package com.postsquad.scoup.web.auth.exception;

public class OAuthException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Failed to get user data from the resource server.";

    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
