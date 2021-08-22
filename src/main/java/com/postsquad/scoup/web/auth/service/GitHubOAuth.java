package com.postsquad.scoup.web.auth.service;

import com.postsquad.scoup.web.auth.controller.request.AccessTokenRequest;
import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.GitHubUserResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponseMapper;
import com.postsquad.scoup.web.auth.exception.AccessTokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.OAuthUserNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
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

    public SocialAuthenticationResponse readOAuthUserData(String type, String code) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        AccessTokenResponse token = getToken(property, code);
        return getOAuthUserInfo(property, token.getAccessToken(), type);
    }

    public SocialAuthenticationResponse readOAuthUserDataFromHeader(String type, String header) {
        OAuthProperty property = oAuthProperties.getProperty(type);
        String accessToken = header.substring(TOKEN.length()).trim();
        return getOAuthUserInfo(property, accessToken, type);
    }

    @Override
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
                        .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                        .bodyToMono(AccessTokenResponse.class)
                        .blockOptional()
                        .orElseThrow(() -> new OAuthException(new AccessTokenNotFoundException()));
    }

    @Override
    protected SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, String accessToken, String type) {
        GitHubUserResponse gitHubUserResponse = webClient.get()
                                                         .uri(oAuthProperty.getUserUri())
                                                         .accept(MediaType.APPLICATION_JSON)
                                                         .header(HttpHeaders.AUTHORIZATION, TOKEN + " " + accessToken)
                                                         .retrieve()
                                                         .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(() -> new OAuthException(new OAuthRequestNotValidException())))
                                                         .bodyToMono(GitHubUserResponse.class)
                                                         .blockOptional()
                                                         .orElseThrow(() -> new OAuthException(new OAuthUserNotFoundException()));
        return SocialAuthenticationResponseMapper.INSTANCE.GitHubUserResponseToSocialAuthenticationResponse(gitHubUserResponse, type);
    }
}
