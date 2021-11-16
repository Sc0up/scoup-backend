package com.postsquad.scoup.web.auth.service;

import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.auth.controller.request.TokenRequestWithGrantType;
import com.postsquad.scoup.web.auth.controller.response.GoogleUserResponse;
import com.postsquad.scoup.web.auth.controller.response.TokenResponse;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.auth.mapper.SocialAuthenticationResponseMapper;
import com.postsquad.scoup.web.auth.exception.TokenNotFoundException;
import com.postsquad.scoup.web.auth.exception.OAuthRequestNotValidException;
import com.postsquad.scoup.web.auth.exception.OAuthException;
import com.postsquad.scoup.web.auth.property.OAuthProperties;
import com.postsquad.scoup.web.auth.property.OAuthProperty;
import com.postsquad.scoup.web.auth.OAuthType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

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
    protected SocialAuthenticationResponse getOAuthUserInfo(OAuthProperty oAuthProperty, TokenResponse token, OAuthType type) {
        String idToken = token.getIdToken();
        String sub, name, email, picture;
        sub = name = email = picture = "";

        try {
            SignedJWT jwt = SignedJWT.parse(idToken);
            Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();
            sub = claims.get("sub").toString();
            name = claims.get("name").toString();
            email = claims.get("email").toString();
            picture = claims.get("picture").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GoogleUserResponse googleUserResponse = new GoogleUserResponse(sub, name, email, picture);
        return SocialAuthenticationResponseMapper.INSTANCE.googleUserResponseToSocialAuthenticationResponse(googleUserResponse, type);
    }
}
