package com.postsquad.scoup.web.auth.exception;

public class OAuthRequestNotValidException extends OAuthException {

    public OAuthRequestNotValidException() {
        super("OAuth request fails validation");
    }
}
