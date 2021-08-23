package com.postsquad.scoup.web.auth.controller.response;

import com.postsquad.scoup.web.auth.property.OAuthType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SocialAuthenticationResponse {

    private OAuthType oAuthType;

    private String socialServiceId;

    private String username;

    private String email;

    private String avatarUrl;
}
