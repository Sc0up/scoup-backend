package com.postsquad.scoup.web.user.controller.response;

import com.postsquad.scoup.web.user.auth.OAuthUserResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialAuthenticationResponse {

    private String socialServiceId;
    private String username;
    private String email;
    private String avatarUrl;

    public static SocialAuthenticationResponse from(OAuthUserResponse oAuthUserResponse) {
        return SocialAuthenticationResponse.builder()
                .socialServiceId(String.valueOf(oAuthUserResponse.getId()))
                .username(oAuthUserResponse.getLogin())
                .email(oAuthUserResponse.getEmail())
                .avatarUrl(oAuthUserResponse.getAvatarUrl())
                .build();
    }
}
