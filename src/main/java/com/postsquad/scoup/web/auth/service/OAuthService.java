package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.OAuthType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final Map<String, OAuth> oAuthMap;

    public SocialAuthenticationResponse readOAuthUserData(String type, String code) {
        OAuth oAuth = oAuthMap.get(type.toLowerCase());
        return oAuth.readOAuthUserData(OAuthType.valueOf(type.toUpperCase()), code);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(String type, String header) {
        OAuth oAuth = oAuthMap.get(type.toLowerCase());
        return oAuth.readOAuthUserDataFromHeader(OAuthType.valueOf(type.toUpperCase()), header);
    }
}
