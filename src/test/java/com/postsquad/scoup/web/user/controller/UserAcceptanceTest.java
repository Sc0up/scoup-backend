package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.User;
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
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class UserAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("signUpProvider")
    @DisplayName("신규 사용자는 이메일을 통해 회원가입을 할 수 있다.")
    void signUp(String description, SignUpRequest givenSignUpRequest, User expectedUser) {
        // given
        String path = "/api/sign-up";
        RequestSpecification givenRequest = RestAssured.given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(path)
                .contentType(ContentType.JSON)
                .body(givenSignUpRequest);

        // when
        Response actualResponse = givenRequest.when()
                .log().all(true)
                .post();

        // then
        actualResponse.then()
                .statusCode(HttpStatus.CREATED.value());
        then(userRepository.findById(actualResponse.body().as(long.class)).orElse(null))
                .as("회원가입 결과 : %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForResponseWithId)
                .isEqualTo(expectedUser);
    }

    static Stream<Arguments> signUpProvider() {
        return Stream.of(
                Arguments.of(
                        "성공",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email@email")
                                .password("password")
                                .build(),
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email@email")
                                .password("password")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("signUpWithValidationProvider")
    @DisplayName("회원가입 DTO validation")
    void signUpWithValidation(String description, SignUpRequest givenSignUpRequest, ErrorResponse expectedResponse) {
        // given
        String path = "/api/sign-up";
        RequestSpecification givenRequest = RestAssured.given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(path)
                .contentType(ContentType.JSON)
                .body(givenSignUpRequest);

        // when
        Response actualResponse = givenRequest.when()
                .log().all(true)
                .post();

        //then
        actualResponse.then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("회원가입 결과 : %s", description)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    static Stream<Arguments> signUpWithValidationProvider() {
        return Stream.of(
                Arguments.of(
                        "실패 - nickname 없음",
                        SignUpRequest.builder()
                                .username("username")
                                .email("email@email")
                                .password("password")
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList("nickname: 비어 있을 수 없습니다"))
                                .build()
                ), Arguments.of(
                        "실패 - username 없음",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .email("email@email")
                                .password("password")
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList("username: 비어 있을 수 없습니다"))
                                .build()
                ), Arguments.of(
                        "실패 - email 없음",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .username("username")
                                .password("password")
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList("email: 비어 있을 수 없습니다"))
                                .build()
                ), Arguments.of(
                        "실패 - password 없음",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email@email")
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList("password: 비어 있을 수 없습니다"))
                                .build()
                ), Arguments.of(
                        "실패 - email 형식 다름",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList("email: 올바른 형식의 이메일 주소여야 합니다"))
                                .build()
                ), Arguments.of(
                        "실패 - 모두 없음",
                        SignUpRequest.builder()
                                .build(),
                        ErrorResponse.builder()
                                .message("Bad Request")
                                .statusCode(400)
                                .errors(Arrays.asList(
                                        "nickname: 비어 있을 수 없습니다",
                                        "email: 비어 있을 수 없습니다",
                                        "password: 비어 있을 수 없습니다",
                                        "username: 비어 있을 수 없습니다"
                                )).build()
                )
        );
    }
}
