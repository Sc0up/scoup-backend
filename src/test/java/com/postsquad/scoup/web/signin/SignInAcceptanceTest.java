package com.postsquad.scoup.web.signin;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("이메일 계정으로 로그인 한다")
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
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        /*
         * TODO
         *  - 리프래쉬토큰
         *  - DTO
         *   - 액세스 토큰 -> exp 달라 verify는 되지만 assertion불가.
         */
        SignedJWT actualRefreshToken = SignedJWT.parse(actualResponse.cookie("refresh"));

        then(actualRefreshToken.verify(macVerifier()))
                .as("로그인 결과(리프래쉬토큰 서명 검증) : %s", description)
                .isTrue();
        then(actualRefreshToken.getJWTClaimsSet().getSubject())
                .as("로그인 결과(리프래쉬토큰 sub) : %s", description)
                // TODO: 실제 userid와 비교 필요
                .isEqualTo("userid");

        then(actualRefreshToken.getJWTClaimsSet().getExpirationTime())
                .as("로그인 결과(리프래쉬토큰 sub) : %s", description)
                .isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .isBefore(LocalDateTime.now().plusWeeks(2).toInstant(ZoneOffset.UTC));

        then(actualResponse.as(SignInResponse.class))
                .as("로그인 결과 : %s", description)
                .usingRecursiveComparison()
                .ignoringFields("accessToken")
                .isEqualTo(expectedSignInResponse);

        checkAccessToken(actualResponse.as(SignInResponse.class).getAccessToken(), "userid");
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

    private void checkAccessToken(String token, String userId) throws ParseException, JOSEException {
        SignedJWT actualRefreshToken = SignedJWT.parse(token);
        actualRefreshToken.verify(macVerifier());
        JWTClaimsSet jwtClaimsSet = actualRefreshToken.getJWTClaimsSet();

        jwtClaimsSet.getSubject();
        assertThat(jwtClaimsSet.getSubject()).isEqualTo(userId);
        assertThat(jwtClaimsSet.getExpirationTime())
                .isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .isBefore(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC));
    }

    private JWSVerifier macVerifier() throws JOSEException {
        return new MACVerifier(jwtSecret);
    }
}
