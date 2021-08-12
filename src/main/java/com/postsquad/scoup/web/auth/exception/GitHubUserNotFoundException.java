package com.postsquad.scoup.web.auth.exception;

public class GitHubUserNotFoundException extends OAuthException {

    public GitHubUserNotFoundException() {
        super("GitHub user not found.");
    }
}
