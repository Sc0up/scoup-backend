package com.postsquad.scoup.web.auth.exception;

public class AccessTokenNotFoundException extends OAuthException {

    public AccessTokenNotFoundException() {
        super("Access token not found.");
    }
}
