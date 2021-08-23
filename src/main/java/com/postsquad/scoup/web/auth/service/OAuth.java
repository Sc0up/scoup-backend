package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.response.TokenResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import com.postsquad.scoup.web.auth.OAuthType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public abstract class OAuth {

    public static final String TOKEN = "token";

    protected final WebClient webClient;

    protected final OAuthProperties oAuthProperties;

    public SocialAuthenticationResponse readOAuthUserData(OAuthType type, String code) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        return getOAuthUserInfo(property, getToken(property, code), type);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(OAuthType type, String header) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        String accessToken = header.substring(TOKEN.length()).trim();
        return getOAuthUserInfo(property, new TokenResponse(accessToken), type);
    }

    abstract TokenResponse getToken(OAuthProperty oAuthProperty, String code);

    abstract SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, TokenResponse accessToken, OAuthType type);
}
