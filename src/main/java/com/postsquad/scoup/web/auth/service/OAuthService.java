package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.request.AccessTokenRequest;
import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.OAuthUserResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.exception.AccessTokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.GitHubRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.GitHubUserNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OAuthService {

    public static final String TOKEN = "token";
    private final WebClient webClient;
    private final OAuthProperties oAuthProperties;

    public SocialAuthenticationResponse readOAuthUserData(String type, String code) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        AccessTokenResponse token = getToken(property, code);
        OAuthUserResponse oAuthUserInfo = getOAuthUserInfo(property, token.getAccessToken());
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(String type, String header) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        String accessToken = header.substring(TOKEN.length()).trim();
        OAuthUserResponse oAuthUserInfo = getOAuthUserInfo(property, accessToken);
        return SocialAuthenticationResponse.from(oAuthUserInfo);
    }

    private AccessTokenResponse getToken(OAuthProperty oAuthProperty, String code) {
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                                                                  .clientId(oAuthProperty.getClientId())
                                                                  .clientSecret(oAuthProperty.getClientSecret())
                                                                  .code(code)
                                                                  .build();

        return webClient.post()
                        .uri(oAuthProperty.getAccessTokenUri())
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(accessTokenRequest)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new GitHubRequestNotValidException())))
                        .bodyToMono(AccessTokenResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new AccessTokenNotFoundException()));
    }


    private OAuthUserResponse getOAuthUserInfo(OAuthProperty oAuthProperty, String accessToken) {
        return webClient.get()
                        .uri(oAuthProperty.getUserUri())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN + " " + accessToken)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new GitHubRequestNotValidException())))
                        .bodyToMono(OAuthUserResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new GitHubUserNotFoundException()));
    }
}
