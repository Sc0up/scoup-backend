package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.domain.User;

public class OAuthUserAlreadyExistsException extends UserAlreadyExistsException {
    public OAuthUserAlreadyExistsException(User user) {
        super(user.getFirstRegisteredOAuthType() + " account already exists");
    }
}
