package com.postsquad.scoup.web.signin.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@RequestMapping("/sign-in")
@RestController
public class SignInController {

    @Value("${jwt.secret.mac}")
    private String jwtSecret;

    @PostMapping
    public SignInResponse signIn(HttpServletResponse response) throws JOSEException {
        Cookie cookie = new Cookie("refresh", refreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeForRefreshToken());
        cookie.setPath("/api"); // TODO: 로그인 이후 동작에서 잘 작동하는지 검증 필요

        response.addCookie(cookie);

        return SignInResponse.builder()
                             .accessToken("accessToken")
                             .nickname("nickname")
                             .email("email")
                             .build();
    }

    private String refreshToken() throws JOSEException {
        JWSSigner jwsSigner = new MACSigner(jwtSecret);
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // TODO: 실제 userid와 연결 필요
        JWTClaimsSet jwtClaimsSet = claimsSetForRefreshToken("userid", expirationTimeForRefreshToken());

        SignedJWT signedJWT = new SignedJWT(
                jwsHeader,
                jwtClaimsSet
        );

        signedJWT.sign(jwsSigner);
        return signedJWT.serialize();
    }

    private JWTClaimsSet claimsSetForRefreshToken(String userId, Date expirationTime) {
        return new JWTClaimsSet.Builder()
                .subject(userId)
                .expirationTime(expirationTime)
                .build();
    }

    private LocalDateTime expirationDateTimeForRefreshToken() {
        return LocalDateTime.now()
                            .plusWeeks(2);
    }

    private Date expirationTimeForRefreshToken() {
        LocalDateTime expirationDateTime = expirationDateTimeForRefreshToken();

        return Date.from(expirationDateTime.toInstant(ZoneOffset.UTC));
    }

    private int maxAgeForRefreshToken() {
        return (int) Duration.between(LocalDateTime.now(), expirationDateTimeForRefreshToken()).toSeconds();
    }
}
