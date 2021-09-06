package com.postsquad.scoup.web.signin.exception;

// TODO: 추후에 상속 구조 올바른지 파악 필요. UserNotFound면 User도메인에 속해야 하는게 아닌지?
public class UserNotFoundException extends SignInFailedException {

    public UserNotFoundException(String email) {
        super("User '" + email + "' not exists");
    }
}
