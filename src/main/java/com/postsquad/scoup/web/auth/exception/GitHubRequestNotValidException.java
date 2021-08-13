package com.postsquad.scoup.web.auth.exception;

public class GitHubRequestNotValidException extends OAuthException {

    public GitHubRequestNotValidException() {
        super("GitHub request is not valid.");
    }
}
