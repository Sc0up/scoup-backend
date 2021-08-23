package com.postsquad.scoup.web.auth.controller.request;

import com.postsquad.scoup.web.auth.property.OAuthProperty;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class TokenRequestWithGrantType {

    private static final String GRANT_TYPE = "authorization_code";

    private static final MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();

    public static MultiValueMap<String, String> of(OAuthProperty oAuthProperty, String code) {
        tokenRequest.add("grant_type", GRANT_TYPE);
        tokenRequest.add("client_id", oAuthProperty.getClientId());
        tokenRequest.add("redirect_uri", oAuthProperty.getRedirectUri());
        tokenRequest.add("client_secret", oAuthProperty.getClientSecret());
        tokenRequest.add("code", code);
        return tokenRequest;
    }
}
