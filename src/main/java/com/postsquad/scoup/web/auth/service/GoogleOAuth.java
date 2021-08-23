package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.request.TokenRequestWithGrantType;
import com.postsquad.scoup.web.auth.controller.response.GoogleUserResponse;
import com.postsquad.scoup.web.auth.controller.response.TokenResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponseMapper;
import com.postsquad.scoup.web.auth.exception.OAuthUserNotFoundException;
import com.postsquad.scoup.web.auth.exception.TokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("google")
public class GoogleOAuth extends OAuth {

    public GoogleOAuth(WebClient webClient, OAuthProperties oAuthProperties) {
        super(webClient, oAuthProperties);
    }

    protected TokenResponse getToken(OAuthProperty oAuthProperty, String code) {
        MultiValueMap<String, String> tokenRequest = TokenRequestWithGrantType.of(oAuthProperty, code);
        return webClient.post()
                        .uri(oAuthProperty.getAccessTokenUri())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .bodyValue(tokenRequest)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                        .bodyToMono(TokenResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new TokenNotFoundException()));
    }

    @Override
    protected SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, TokenResponse token, String type) {
        GoogleUserResponse googleUserResponse = webClient.post()
                                                         .uri(oAuthProperty.getUserUri())
                                                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                                         .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                                                         .retrieve()
                                                         .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                                                         .bodyToMono(GoogleUserResponse.class)
                                                         .blockOptional()
                                                         .orElseThrow(() -> new OAuthException(new OAuthUserNotFoundException()));
        return SocialAuthenticationResponseMapper.INSTANCE.googleUserResponseToSocialAuthenticationResponse(googleUserResponse, type);
    }
}
