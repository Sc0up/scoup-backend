package com.postsquad.scoup.web.auth.exception;

public class InvalidGitHubRequestException extends OAuthException {

    public InvalidGitHubRequestException() {
        super("GitHub request is not valid.");
    }
}
