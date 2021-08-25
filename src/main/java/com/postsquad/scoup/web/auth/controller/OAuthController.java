package com.postsquad.scoup.web.auth.controller;

import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.service.OAuthService;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/user-data")
    public SocialAuthenticationResponse readOAuthUserData(@RequestParam String type, @RequestParam String code) {
        return oAuthService.readOAuthUserData(type, code);
    }

    @GetMapping("/user-data/token")
    public SocialAuthenticationResponse readOAuthUserDataWithToken(@RequestParam String type, @RequestHeader("Authorization") String header) {
        return oAuthService.readOAuthUserDataFromHeader(type, header);
    }

    @ExceptionHandler(OAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse oAuthExceptionHandler(OAuthException oAuthException) {
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, oAuthException.getMessage(), oAuthException.getCause().getMessage());
    }
}
