package com.postsquad.scoup.web.auth.exception;

public class OAuthUserNotFoundException extends OAuthException {

    public OAuthUserNotFoundException() {
        super("OAuth user not found");
    }
}
