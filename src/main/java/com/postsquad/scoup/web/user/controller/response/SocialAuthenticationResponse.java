package com.postsquad.scoup.web.user.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialAuthenticationResponse {

    private String username;

    private String email;

    private String avatarUrl;
}
