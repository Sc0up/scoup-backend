package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.GitHubUserResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class OAuthService {

    public static final String TOKEN = "token";

    @Autowired
    protected WebClient webClient;

    @Autowired
    protected OAuthProperties oAuthProperties;

    public SocialAuthenticationResponse readOAuthUserData(String type, String code) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        AccessTokenResponse token = getToken(property, code);
        GitHubUserResponse oAuthUserInfo = getOAuthUserInfo(property, token.getAccessToken());
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(String type, String header) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        String accessToken = header.substring(TOKEN.length()).trim();
        GitHubUserResponse oAuthUserInfo = getOAuthUserInfo(property, accessToken);
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }

    abstract AccessTokenResponse getToken(OAuthProperty oAuthProperty, String code);

    abstract GitHubUserResponse getOAuthUserInfo(OAuthProperty oAuthProperty, String accessToken);
}
