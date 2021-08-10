package com.postsquad.scoup.web.user.auth.exception;

import com.postsquad.scoup.web.user.auth.exception.OAuthException;

public class AccessTokenNotFoundException extends OAuthException {

    public AccessTokenNotFoundException() {
        super("Access token not found.");
    }
}
