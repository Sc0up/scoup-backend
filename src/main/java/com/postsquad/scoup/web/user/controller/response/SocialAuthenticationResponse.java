package com.postsquad.scoup.web.user.controller.response;

import com.postsquad.scoup.web.auth.response.OAuthUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
