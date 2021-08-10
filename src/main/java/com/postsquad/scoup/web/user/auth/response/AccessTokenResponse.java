package com.postsquad.scoup.web.user.auth.response;

import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private String accessToken;
    private String tokenType;
    private String scope;
}
