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

    public SocialAuthenticationResponse readOAuthUserData(OAuthType type, String code) {
        OAuth oAuth = oAuthMap.get(type.name().toLowerCase());
        return oAuth.readOAuthUserData(type, code);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(OAuthType type, String header) {
        OAuth oAuth = oAuthMap.get(type.name().toLowerCase());
        return oAuth.readOAuthUserDataFromHeader(type, header);
    }
}
