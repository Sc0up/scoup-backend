package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.domain.User;

public class EmailAlreadyExistsException extends UserAlreadyExistsException {
    public EmailAlreadyExistsException(User user) {
        super("User email '" + user.getEmail() + "' already exists");
    }
}
