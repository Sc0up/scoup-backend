package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.domain.User;

public class UserAlreadyExistsException extends SignUpFailedException {
    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(User user) {
        super("User(" + user.getEmail() + ") already exists");
    }
}
