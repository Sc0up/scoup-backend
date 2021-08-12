package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class UserAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                                    .nickname("existing")
                                    .username("username")
                                    .email("existing@email.com")
                                    .avatarUrl("avatarUrl")
                                    .password("password")
                                    .build());
    }

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
                .ignoringFields(new String[]{"createdDateTime", "modifiedDateTime", "id"})
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
                                                    .header("Accept-Language", "ko-KR")
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
                .ignoringFields("timestamp")
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

    @ParameterizedTest
    @MethodSource("validateEmailProvider")
    @DisplayName("이미 가입된 이메일을 입력할 경우 이메일이 중복되었다는 메시지가 반환된다.")
    void validateEmail(String description, String givenEmailRequest, EmailValidationResponse expectedEmailResponse) {
        // given
        String path = "/api/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                    .baseUri(BASE_URL)
                                                    .port(port)
                                                    .basePath(path)
                                                    .queryParam("email", givenEmailRequest);

        // when
        Response actualResponse = givenRequest.when()
                                          .log().all()
                                          .get()
                                          .andReturn();

        // then
        actualResponse.then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(EmailValidationResponse.class))
                .as("이메일 중복 확인: %s", description)
                .usingRecursiveComparison()
                .isEqualTo(expectedEmailResponse);
    }

    static Stream<Arguments> validateEmailProvider() {
        return Stream.of(
                Arguments.of("성공: 중복된 이메일",
                        "existing@email.com",
                        EmailValidationResponse.valueOf(true)
                ),
                Arguments.of("성공: 중복되지 않은 이메일",
                        "notExisting@email.com",
                        EmailValidationResponse.valueOf(false)
                ));
    }

    @ParameterizedTest
    @MethodSource("validateRequestParamProvider")
    @DisplayName("Request parameter validation")
    void validateRequestParam(String description, String givenEmailRequest, ErrorResponse expectedResponse) {
        // given
        String path = "/api/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                    .baseUri(BASE_URL)
                                                    .port(port)
                                                    .basePath(path)
                                                    .queryParam("email", givenEmailRequest);

        // when
        Response actualResponse = givenRequest.when()
                                          .log().all()
                                          .get()
                                          .andReturn();

        // then
        actualResponse.then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("이메일 중복 확인: %s", description)
                .usingRecursiveComparison()
                .ignoringFields("timestamp")
                .isEqualTo(expectedResponse);
    }

    static Stream<Arguments> validateRequestParamProvider() {
        return Stream.of(
                Arguments.of("실패: 빈 문자열",
                        "",
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("Bad Request")
                                .errors(Collections.singletonList("validateEmail.email: must not be empty"))
                                .build()),
                Arguments.of("실패: null",
                        null,
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("Bad Request")
                                .errors(Collections.singletonList("validateEmail.email: must not be empty"))
                                .build()),
                Arguments.of("실패: 이메일 형식",
                        "email",
                        ErrorResponse.builder()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("Bad Request")
                                .errors(Collections.singletonList("validateEmail.email: must be a well-formed email address"))
                                .build())
        );
    }
}
