package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.controller.response.NicknameValidationResponse;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.provider.*;
import com.postsquad.scoup.web.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

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

    @ParameterizedTest
    @ArgumentsSource(EmailSignUpProvider.class)
    @DisplayName("신규 사용자는 이메일을 통해 회원가입을 할 수 있다")
    void emailSignUp(String description, SignUpRequest givenSignUpRequest, User expectedUser) {
        signUp(description, givenSignUpRequest, expectedUser);
    }

    @ParameterizedTest
    @ArgumentsSource(SocialSignUpProvider.class)
    @DisplayName("신규 사용자는 소셜 서비스를 통해 회원가입을 할 수 있다")
    void socialSignUp(String description, SignUpRequest givenSignUpRequest, User expectedUser) {
        signUp(description, givenSignUpRequest, expectedUser);
    }

    private void signUp(String description, SignUpRequest givenSocialSignUpRequest, User expectedUser) {
        // given
        String path = "/api/users";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenSocialSignUpRequest);

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

    @ParameterizedTest
    @ArgumentsSource(SignUpViaEmailWhenUserAlreadyExistsProvider.class)
    @DisplayName("기사용자가 회원가입을 한 경우 회원가입이 되지 않는다 - 이메일")
    void signUpViaEmailWhenUserAlreadyExists(String description, SignUpRequest givenSignUpRequestAlreadyExists, ErrorResponse expectedResponse) {
        signUpWhenUserAlreadyExists(description, givenSignUpRequestAlreadyExists, expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(SignUpViaSocialWhenUserAlreadyExistsProvider.class)
    @DisplayName("기사용자가 회원가입을 한 경우 회원가입이 되지 않는다 - 소셜")
    void signUpViaSocialWhenUserAlreadyExists(String description, SignUpRequest givenSignUpRequestAlreadyExists, ErrorResponse expectedResponse) {
        userRepository.save(User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email@email.com")
                                .avatarUrl("avatarUrl")
                                .password("password")
                                .oAuthUsers(List.of(OAuthUser.of(OAuthType.GITHUB, "1234567")))
                                .build());
        signUpWhenUserAlreadyExists(description, givenSignUpRequestAlreadyExists, expectedResponse);
    }

    private void signUpWhenUserAlreadyExists(String description, SignUpRequest givenSignUpRequestAlreadyExists, ErrorResponse expectedResponse) {
        // given
        String path = "/api/users";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenSignUpRequestAlreadyExists);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .post();

        // then
        actualResponse.then()
                      .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("회원가입 결과 - %s", description)
                .usingRecursiveComparison()
                .ignoringFields(new String[]{"timestamp"})
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(SignUpWithValidationProvider.class)
    @DisplayName("회원가입 DTO validation")
    void signUpWithValidation(String description, SignUpRequest givenSignUpRequest, ErrorResponse expectedResponse) {
        // given
        String path = "/api/users";
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
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateEmailProvider.class)
    @DisplayName("이미 가입된 이메일을 입력할 경우 이메일이 중복되었다는 메시지가 반환된다")
    void validateEmail(String description, String givenEmail, EmailValidationResponse expectedEmailValidationResponse) {
        // given
        String path = "/api/users/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .queryParam("email", givenEmail);

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
                .isEqualTo(expectedEmailValidationResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateEmailRequestParamProvider.class)
    @DisplayName("email as RequestParam validation")
    void validateEmailAsRequestParam(String description, String invalidEmail, ErrorResponse expectedResponse) {
        // given
        String path = "/api/users/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .header("Accept-Language", "en-US")
                                                       .queryParam("email", invalidEmail);

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
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateNicknameProvider.class)
    @DisplayName("이미 가입된 닉네임을 입력할 경우 닉네임이 중복되었다는 메시지가 반환된다")
    void validateNickname(String description, String givenNickname, NicknameValidationResponse expectedNicknameValidationResponse) {
        // given
        String path = "/api/users/validate/nickname";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .queryParam("nickname", givenNickname);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all()
                                              .get()
                                              .andReturn();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(NicknameValidationResponse.class))
                .as("닉네임 중복 확인: %s", description)
                .usingRecursiveComparison()
                .isEqualTo(expectedNicknameValidationResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateNicknameRequestParamProvider.class)
    @DisplayName("nickname as RequestParam validation")
    void validateNicknameAsRequestParam(String description, String invalidNickname, ErrorResponse expectedResponse) {
        // given
        String path = "/api/users/validate/nickname";
        RequestSpecification givenRequest = RestAssured.given()
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .header("Accept-Language", "en-US")
                                                       .queryParam("nickname", invalidNickname);

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
                .as("닉네임 중복 확인: ", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }
}
