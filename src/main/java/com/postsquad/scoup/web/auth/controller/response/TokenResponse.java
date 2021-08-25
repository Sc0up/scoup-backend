package com.postsquad.scoup.web.auth.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenResponse {

    private String accessToken;

    private String idToken;

    private String tokenType;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
