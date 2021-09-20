package com.postsquad.scoup.web.signin.exception;

public class UnauthorizedUserException extends AuthorizationFailedException {

    public UnauthorizedUserException() {
        super("Access denied");
    }
}
