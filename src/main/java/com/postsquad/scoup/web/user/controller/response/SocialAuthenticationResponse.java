package com.postsquad.scoup.web.user.controller.response;

import lombok.Getter;

@Getter
public class SocialAuthenticationResponse {

    private String username;

    private String email;

    private String avatarUrl;
}
