package com.postsquad.scoup.web.signin.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.signin.mapper.SignInResponseMapper;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class SignInService {

    private final UserRepository userRepository;

    @Value("${jwt.secret.mac}")
    private String jwtSecret;


    public SignInResponse signIn(SignInRequest signInRequest) throws JOSEException {

        User user = userRepository.findByEmail(signInRequest.getEmail())
                                  // TODO: 2-2-2에서 검증
                                  .orElseThrow();

        if (!user.getPassword().equals(signInRequest.getPassword())) {
            // TODO: 2-2-2에서 검증
        }

        return SignInResponseMapper.INSTANCE.userToSignInResponse(user, accessToken());
    }

    private String accessToken() throws JOSEException {
        JWSSigner jwsSigner = new MACSigner(jwtSecret);
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // TODO: 실제 userid와 연결 필요
        JWTClaimsSet jwtClaimsSet = claimsSetForAccessToken("userid");

        SignedJWT signedJWT = new SignedJWT(
                jwsHeader,
                jwtClaimsSet
        );

        signedJWT.sign(jwsSigner);
        return signedJWT.serialize();
    }

    private JWTClaimsSet claimsSetForAccessToken(String userId) {
        return new JWTClaimsSet.Builder()
                .subject(userId)
                .expirationTime(expirationTimeForAccessToken())
                .build();
    }

    private LocalDateTime expirationDateTimeForAccessToken() {
        return LocalDateTime.now()
                            .plusMinutes(30);
    }

    private Date expirationTimeForAccessToken() {
        LocalDateTime expirationDateTime = expirationDateTimeForAccessToken();

        return Date.from(expirationDateTime.toInstant(ZoneOffset.UTC));
    }
}
