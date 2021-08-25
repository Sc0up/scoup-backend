package com.postsquad.scoup.web.auth.controller.response;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SocialAuthenticationResponse {

    private OAuthType oAuthType;

    private String socialServiceId;

    private String name;

    private String email;

    private String avatarUrl;
}
