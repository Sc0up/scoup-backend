package com.postsquad.scoup.web.auth.controller.response;

import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private String accessToken;

    private String tokenType;
}
