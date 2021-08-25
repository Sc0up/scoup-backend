package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.request.TokenRequest;
import com.postsquad.scoup.web.auth.controller.response.TokenResponse;
import com.postsquad.scoup.web.auth.controller.response.GitHubUserResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponseMapper;
import com.postsquad.scoup.web.auth.exception.TokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.OAuthUserNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import com.postsquad.scoup.web.auth.OAuthType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("github")
public class GitHubOAuth extends OAuth {

    public GitHubOAuth(WebClient webClient, OAuthProperties oAuthProperties) {
        super(webClient, oAuthProperties);
    }

    @Override
    protected TokenResponse getToken(OAuthProperty oAuthProperty, String code) {
        TokenRequest tokenRequest = TokenRequest.builder()
                                                .clientId(oAuthProperty.getClientId())
                                                .clientSecret(oAuthProperty.getClientSecret())
                                                .redirectUri(oAuthProperty.getRedirectUri())
                                                .code(code)
                                                .build();

        return webClient.post()
                        .uri(oAuthProperty.getAccessTokenUri())
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(tokenRequest)
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                        .bodyToMono(TokenResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new TokenNotFoundException()));
    }

    @Override
    protected SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, TokenResponse token, OAuthType type) {
        GitHubUserResponse gitHubUserResponse = webClient.get()
                                                         .uri(oAuthProperty.getUserUri())
                                                         .accept(MediaType.APPLICATION_JSON)
                                                         .header(HttpHeaders.AUTHORIZATION, TOKEN + " " + token.getAccessToken())
                                                         .retrieve()
                                                         .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                                                         .bodyToMono(GitHubUserResponse.class)
                                                         .blockOptional()
                                                         .orElseThrow(() -> new OAuthException(new OAuthUserNotFoundException()));
        return SocialAuthenticationResponseMapper.INSTANCE.gitHubUserResponseToSocialAuthenticationResponse(gitHubUserResponse, type);
    }
}
