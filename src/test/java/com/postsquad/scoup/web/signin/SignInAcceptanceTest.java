package com.postsquad.scoup.web.signin;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class SignInAcceptanceTest extends AcceptanceTestBase {

    @Value("${jwt.secret.mac}")
    String jwtSecret;

    private void signUp(SignUpRequest signUpRequest) {
        String path = "/api/users";
        RestAssured.given()
                   .baseUri(BASE_URL)
                   .port(port)
                   .basePath(path)
                   .contentType(ContentType.JSON)
                   .body(signUpRequest)
                   .post();
    }

    @ParameterizedTest
    @MethodSource("signInProvider")
    void signIn(
            String description,
            SignUpRequest givenSignUpRequest,
            SignInRequest givenSignInRequest,
            SignInResponse expectedSignInResponse
    ) throws ParseException, JOSEException {
        // given
        signUp(givenSignUpRequest);
        String path = "/api/sign-in";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenSignInRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .post();
        // then
        actualResponse.then()
                      .statusCode(HttpStatus.OK.value());
        /*
         * TODO
         *  - 리프래쉬토큰
         *  - DTO
         *   - 액세스 토큰
         *   -
         */
        SignedJWT actualRefreshToken = SignedJWT.parse(actualResponse.cookie("refresh"));
        then(actualRefreshToken.verify(macSigner())).isTrue();
        then(actualResponse.as(SignInResponse.class))
                .as("로그인 결과 : %s", description)
                .usingRecursiveComparison()
                .ignoringFields("accessToken.exp")
                .isEqualTo(expectedSignInResponse);

    }

    static Stream<Arguments> signInProvider() {
        return Stream.of(
                Arguments.of(
                        "성공",
                        SignUpRequest.builder()
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email@email")
                                     .password("password")
                                     .build(),
                        SignInRequest.builder()
                                     .email("email@email")
                                     .password("password")
                                     .build(),
                        SignInResponse.builder()
                                      .accessToken("accessToken")
                                      .nickname("nickname")
                                      .email("email")
                                      .build()
                )
        );
    }

    private JWSVerifier macSigner() throws JOSEException {
        return new MACVerifier(jwtSecret);
    }
}
