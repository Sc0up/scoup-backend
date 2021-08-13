package com.postsquad.scoup.web.auth.controller;

import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/oauth/user-data")
    public SocialAuthenticationResponse readOAuthUserData(@RequestParam String code) {
        return oAuthService.readOAuthUserData(code);
    }

    @GetMapping("/oauth/user-data/token")
    public SocialAuthenticationResponse readOAuthUserDataWithToken(@RequestHeader("Authorization") String header) {
        return oAuthService.readOAuthUserDataWithToken(header);
    }
}
