package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.domain.User;

public class NicknameAlreadyExistsException extends UserAlreadyExistsException {
    public NicknameAlreadyExistsException(User user) {
        super("User nickname '" + user.getNickname() + "' already exists");
    }
}
