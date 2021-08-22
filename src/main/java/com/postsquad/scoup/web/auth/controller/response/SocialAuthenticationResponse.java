package com.postsquad.scoup.web.auth.controller.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SocialAuthenticationResponse {

    private String oAuthType;

    private String socialServiceId;

    private String username;

    private String email;

    private String avatarUrl;
}
