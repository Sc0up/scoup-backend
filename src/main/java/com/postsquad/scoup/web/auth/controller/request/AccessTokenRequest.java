package com.postsquad.scoup.web.auth.controller.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenRequest {

    private final String clientId;

    private final String clientSecret;

    private final String code;
}
