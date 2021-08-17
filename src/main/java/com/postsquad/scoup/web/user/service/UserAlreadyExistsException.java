package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.domain.User;

public class UserAlreadyExistsException extends SignUpFailedException {
    //TODO: EmailAlreadyExistsException 추출하는게 더 적절할 것으로 보임
    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(User user) {
        super("User email '" + user.getEmail() + "' already exists");
    }
}
