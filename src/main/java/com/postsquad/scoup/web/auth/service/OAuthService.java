package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.OAuth;
import com.postsquad.scoup.web.auth.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.response.OAuthUserResponse;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final OAuth oauth;

    public SocialAuthenticationResponse readOAuthUserData(String code) {
        AccessTokenResponse token = oauth.getToken(code);
        OAuthUserResponse oAuthUserInfo = oauth.getOAuthUserInfo(token.getAccessToken());
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }

    public SocialAuthenticationResponse readOAuthUserDataWithToken(String header) {
        OAuthUserResponse oAuthUserInfo = oauth.getOAuthUserInfoFromHeader(header);
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }
}
