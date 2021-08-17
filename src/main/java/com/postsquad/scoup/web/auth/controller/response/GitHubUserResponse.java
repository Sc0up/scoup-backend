package com.postsquad.scoup.web.auth.controller.response;

import lombok.Getter;

@Getter
public class GitHubUserResponse {

    private Long id;

    private String login;

    private String email;

    private String avatarUrl;
}
