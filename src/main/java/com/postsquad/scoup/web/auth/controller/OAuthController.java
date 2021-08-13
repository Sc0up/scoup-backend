package com.postsquad.scoup.web.auth.controller;

import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.service.OAuthService;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler(OAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse oAuthExceptionHandler(OAuthException oAuthException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, oAuthException.getMessage(), oAuthException.getCause().getMessage());
    }
}
