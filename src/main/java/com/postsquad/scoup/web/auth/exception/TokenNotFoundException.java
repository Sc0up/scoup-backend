package com.postsquad.scoup.web.auth.exception;

public class TokenNotFoundException extends OAuthException {

    public TokenNotFoundException() {
        super("Token not found.");
    }
}
