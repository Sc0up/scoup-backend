package com.postsquad.scoup.web.auth.controller.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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
