package com.postsquad.scoup.web.user.controller;

import com.postsquad.scoup.web.AcceptanceTestBase;
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
    void signUp(String desc, SignUpRequest givenSignUpRequest, User expectedUser) {
        String path = "/api/sign-up";
        RequestSpecification givenRequest = RestAssured.given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(path)
                .contentType(ContentType.JSON)
                .body(givenSignUpRequest);

        Response actualResponse = givenRequest.when()
                .log().all(true)
                .post();

        actualResponse.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
        then(userRepository.findById(expectedUser.getId()).orElse(null))
                .as("회원가입 결과 : %s", desc)
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    static Stream<Arguments> signUpProvider() {
        return Stream.of(
                Arguments.of(
                        "성공",
                        SignUpRequest.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build(),
                        User.builder()
                                .id(1L)
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build()
                )
        );
    }
}
