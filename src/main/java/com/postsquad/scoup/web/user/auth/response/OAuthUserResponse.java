package com.postsquad.scoup.web.user.auth.response;

import lombok.Getter;

@Getter
public class OAuthUserResponse {

    private Long id;
    private String login;
    private String email;
    private String avatarUrl;
}
