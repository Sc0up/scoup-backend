package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.request.AccessTokenRequest;
import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.GitHubUserResponse;
import com.postsquad.scoup.web.auth.exception.AccessTokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.GitHubRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.GitHubUserNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GitHubService extends OAuthService {

    protected AccessTokenResponse getToken(OAuthProperty oAuthProperty, String code) {
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                                                                  .clientId(oAuthProperty.getClientId())
                                                                  .clientSecret(oAuthProperty.getClientSecret())
                                                                  .redirectUri(oAuthProperty.getRedirectUri())
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

    protected GitHubUserResponse getOAuthUserInfo(OAuthProperty oAuthProperty, String accessToken) {
        return webClient.get()
                        .uri(oAuthProperty.getUserUri())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN + " " + accessToken)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new GitHubRequestNotValidException())))
                        .bodyToMono(GitHubUserResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new GitHubUserNotFoundException()));
    }
}
