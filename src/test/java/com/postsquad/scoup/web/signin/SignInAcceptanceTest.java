package com.postsquad.scoup.web.signin;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class SignInAcceptanceTest extends AcceptanceTestBase {

    @Value("${jwt.secret.mac}")
    String jwtSecret;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private long signUp(SignUpRequest signUpRequest) {
        String path = "/api/users";
        return RestAssured.given()
                          .baseUri(BASE_URL)
                          .port(port)
                          .basePath(path)
                          .contentType(ContentType.JSON)
                          .body(signUpRequest)
                          .post()
                          .as(int.class);
    }

    private RequestSpecification signInRequest(SignInRequest signInRequest) {
        String path = "/api/sign-in";
        return RestAssured.given()
                          .baseUri(BASE_URL)
                          .port(port)
                          .basePath(path)
                          .contentType(ContentType.JSON)
                          .body(signInRequest);
    }

    @ParameterizedTest
    @MethodSource("signInProvider")
    @DisplayName("이메일 계정으로 로그인 한다 - response body 검증")
    void signInVerifyResponseBody(
            String description,
            SignUpRequest givenSignUpRequest,
            SignInRequest givenSignInRequest,
            SignInResponse expectedSignInResponse
    ) {
        // given
        signUp(givenSignUpRequest);
        RequestSpecification givenRequest = signInRequest(givenSignInRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .post();
        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        then(actualResponse.as(SignInResponse.class))
                .as("로그인 결과 : %s", description)
                .usingRecursiveComparison()
                .ignoringFields("accessToken")
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
                                      .nickname("nickname")
                                      .email("email@email")
                                      .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("signInVerifyTokenProvider")
    @DisplayName("이메일 계정으로 로그인 한다 - access token 검증")
    void signInVerifyAccessToken(
            String description,
            SignUpRequest givenSignUpRequest,
            SignInRequest givenSignInRequest
    ) throws ParseException, JOSEException {
        // given
        long userId = signUp(givenSignUpRequest);
        RequestSpecification givenRequest = signInRequest(givenSignInRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .post();
        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        SignedJWT actualAccessToken = SignedJWT.parse(actualResponse.as(SignInResponse.class).getAccessToken());
        then(actualAccessToken.verify(macVerifier()))
                .as("로그인 결과(access token 서명 검증) : %s", description)
                .isTrue();
        then(actualAccessToken.getJWTClaimsSet().getSubject())
                .as("로그인 결과(access token subject 검증) : %s", description)
                .isEqualTo(String.valueOf(userId));
        then(actualAccessToken.getJWTClaimsSet().getExpirationTime())
                .as("로그인 결과(access token expiration time 검증) : %s", description)
                .isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .isBefore(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC));
    }

    @ParameterizedTest
    @MethodSource("signInVerifyTokenProvider")
    @DisplayName("이메일 계정으로 로그인 한다 - refresh token 검증")
    void signInVerifyRefreshToken(
            String description,
            SignUpRequest givenSignUpRequest,
            SignInRequest givenSignInRequest
    ) throws ParseException, JOSEException {
        // given
        long userId = signUp(givenSignUpRequest);
        RequestSpecification givenRequest = signInRequest(givenSignInRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .post();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());

        SignedJWT actualRefreshToken = SignedJWT.parse(actualResponse.cookie("refresh"));
        then(actualRefreshToken.verify(macVerifier()))
                .as("로그인 결과(refresh token 서명 검증) : %s", description)
                .isTrue();
        then(actualRefreshToken.getJWTClaimsSet().getSubject())
                .as("로그인 결과(refresh token subject 검증) : %s", description)
                .isEqualTo(String.valueOf(userId));
        then(actualRefreshToken.getJWTClaimsSet().getExpirationTime())
                .as("로그인 결과(refresh token expiration time 검증) : %s", description)
                .isAfter(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .isBefore(LocalDateTime.now().plusWeeks(2).toInstant(ZoneOffset.UTC));
    }

    static Stream<Arguments> signInVerifyTokenProvider() {
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
                                     .build()
                )
        );
    }

    private JWSVerifier macVerifier() throws JOSEException {
        // TODO: 서비스에서 검증 필요시 해당 메소드로 대체
        return new MACVerifier(jwtSecret);
    }
}
