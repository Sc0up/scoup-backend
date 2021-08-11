package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.OAuth;
import com.postsquad.scoup.web.auth.response.OAuthUserResponse;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuth oauth;

    public SocialAuthenticationResponse readOAuthUserData(String code) {
        AccessTokenResponse token = oauth.getToken(code);
        OAuthUserResponse userInfo = oauth.getUserInfo(token.getAccessToken());
        return SocialAuthenticationResponse.from(userInfo);
    }

    public SocialAuthenticationResponse readOAuthUserDataWithToken(String header) {
        String token = "token";
        String accessToken = header.substring(token.length()).trim();
        OAuthUserResponse userInfo = oauth.getUserInfo(accessToken);
        return SocialAuthenticationResponse.from(userInfo);
    }
}
