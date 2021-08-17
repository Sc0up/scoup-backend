package com.postsquad.scoup.web.auth.controller.request;

import com.postsquad.scoup.web.auth.property.OAuthProperty;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class AccessTokenRequestWithGrantType {

    public static final String GRANT_TYPE = "authorization_code";
    private static final MultiValueMap<String, String> accessTokenRequest = new LinkedMultiValueMap<>();

    public AccessTokenRequestWithGrantType(OAuthProperty oAuthProperty, String code) {
        accessTokenRequest.add("grant_type", GRANT_TYPE);
        accessTokenRequest.add("client_id", oAuthProperty.getClientId());
        accessTokenRequest.add("redirect_uri", oAuthProperty.getRedirectUri());
        accessTokenRequest.add("client_secret", oAuthProperty.getClientSecret());
        accessTokenRequest.add("code", code);
    }
}
