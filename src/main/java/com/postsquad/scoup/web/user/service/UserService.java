package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.auth.response.AccessTokenResponse;
import com.postsquad.scoup.web.user.auth.OAuth;
import com.postsquad.scoup.web.user.auth.response.OAuthUserResponse;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OAuth oauth;

    public SocialAuthenticationResponse readUserData(String code) {
        AccessTokenResponse token = oauth.getToken(code);
        OAuthUserResponse userInfo = oauth.getUserInfo(token.getAccessToken());
        return SocialAuthenticationResponse.from(userInfo);
    }

    public SocialAuthenticationResponse readUserDataWithToken(String header) {
        String token = "token";
        String accessToken = header.substring(token.length()).trim();
        OAuthUserResponse userInfo = oauth.getUserInfo(accessToken);
        return SocialAuthenticationResponse.from(userInfo);
    }
}
