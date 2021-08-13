package com.postsquad.scoup.web.auth.controller;

import com.postsquad.scoup.web.auth.service.OAuthService;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/social/authenticate")
    public SocialAuthenticationResponse readOAuthUserData(@RequestParam String code) {
        return oAuthService.readOAuthUserData(code);
    }

    @GetMapping("/social/authenticate/token")
    public SocialAuthenticationResponse readOAuthUserDataWithToken(@RequestHeader("Authorization") String header) {
        return oAuthService.readOAuthUserDataWithToken(header);
    }
}
