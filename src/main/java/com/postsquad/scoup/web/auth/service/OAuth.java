package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public abstract class OAuth {

    public static final String TOKEN = "token";

    protected final WebClient webClient;

    protected final OAuthProperties oAuthProperties;

    public SocialAuthenticationResponse readOAuthUserData(String type, String code) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        AccessTokenResponse token = getToken(property, code);
        return getOAuthUserInfo(property, token.getAccessToken(), type);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(String type, String header) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        String accessToken = header.substring(TOKEN.length()).trim();
        return getOAuthUserInfo(property, accessToken, type);
    }

    abstract AccessTokenResponse getToken(OAuthProperty oAuthProperty, String code);

    abstract SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, String accessToken, String type);
}
