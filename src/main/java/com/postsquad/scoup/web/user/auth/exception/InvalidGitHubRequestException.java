package com.postsquad.scoup.web.user.auth.exception;

import com.postsquad.scoup.web.user.auth.exception.OAuthException;

public class InvalidGitHubRequestException extends OAuthException {

    public InvalidGitHubRequestException() {
        super("GitHub request is not valid.");
    }
}
