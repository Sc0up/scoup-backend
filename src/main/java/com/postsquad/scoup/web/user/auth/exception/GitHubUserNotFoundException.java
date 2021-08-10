package com.postsquad.scoup.web.user.auth.exception;

import com.postsquad.scoup.web.user.auth.exception.OAuthException;

public class GitHubUserNotFoundException extends OAuthException {

    public GitHubUserNotFoundException() {
        super("GitHub user not found.");
    }
}
