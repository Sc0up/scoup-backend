package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.EmailValidationRequest;
import com.postsquad.scoup.web.user.controller.request.NicknameValidationRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.restdocs.snippet.Snippet;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

class UserAcceptanceTest extends AcceptanceTestBase {

    private static final Snippet EMAIL_SIGN_UP_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("oauth_type", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("OAuth ?????? (NONE?????? ??????)"),
            fieldWithPathAndConstraints("social_service_id", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("?????? ????????? id (\"\" ??????)"),
            fieldWithPathAndConstraints("nickname", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? ??????(unique)"),
            fieldWithPathAndConstraints("username", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? ??????"),
            fieldWithPathAndConstraints("email", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? email"),
            fieldWithPathAndConstraints("password", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? password"),
            fieldWithPathAndConstraints("avatar_url", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? ????????? url")
                    .optional()
    );

    private static final Snippet SOCIAL_SIGN_UP_REQUEST_FIELDS = requestFields(
            fieldWithPathAndConstraints("oauth_type", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("OAuth ?????? (GITHUB, KAKAO, GOOGLE)"),
            fieldWithPathAndConstraints("social_service_id", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("?????? ????????? id"),
            fieldWithPathAndConstraints("nickname", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? ??????(unique)"),
            fieldWithPathAndConstraints("username", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? ??????"),
            fieldWithPathAndConstraints("email", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? email"),
            fieldWithPathAndConstraints("password", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("????????? password"),
            fieldWithPathAndConstraints("avatar_url", SignUpRequest.class)
                    .type(JsonFieldType.STRING)
                    .description("(optional) ????????? ????????? url")
                    .optional()
    );

    private static final Snippet EMAIL_VALIDATION_REQUEST_PARAMS = requestParameters(
            parameterWithNameAndConstraints("email", EmailValidationRequest.class)
                    .description("?????? ?????????")
    );

    private static final Snippet EMAIL_VALIDATION_RESPONSE_FIELDS = responseFields(
            fieldWithPath("is_existing_email")
                    .type(JsonFieldType.BOOLEAN)
                    .description("????????? ?????? ??????")
    );

    private static final Snippet NICKNAME_VALIDATION_REQUEST_PARAMS = requestParameters(
            parameterWithNameAndConstraints("nickname", NicknameValidationRequest.class)
                    .description("?????? ?????????")
    );

    private static final Snippet NICKNAME_VALIDATION_RESPONSE_FIELDS = responseFields(
            fieldWithPath("is_existing_nickname")
                    .type(JsonFieldType.BOOLEAN)
                    .description("????????? ?????? ??????")
    );

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
    @DisplayName("?????? ???????????? ???????????? ?????? ??????????????? ??? ??? ??????")
    void emailSignUp(String description, SignUpRequest givenSignUpRequest, User expectedUser) {
        signUp(
                description,
                givenSignUpRequest,
                expectedUser,
                document(
                        DEFAULT_RESTDOCS_PATH,
                        EMAIL_SIGN_UP_REQUEST_FIELDS,
                        DEFAULT_POST_RESPONSE_FIELDS
                )
        );
    }

    @ParameterizedTest
    @ArgumentsSource(SocialSignUpProvider.class)
    @DisplayName("?????? ???????????? ?????? ???????????? ?????? ??????????????? ??? ??? ??????")
    void socialSignUp(String description, SignUpRequest givenSignUpRequest, User expectedUser) {
        signUp(
                description,
                givenSignUpRequest,
                expectedUser,
                document(
                        DEFAULT_RESTDOCS_PATH,
                        SOCIAL_SIGN_UP_REQUEST_FIELDS,
                        DEFAULT_POST_RESPONSE_FIELDS
                )
        );
    }

    private void signUp(String description, SignUpRequest givenSocialSignUpRequest, User expectedUser, RestDocumentationFilter document) {
        // given
        String path = "/api/users";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenSocialSignUpRequest);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document)
                                              .log().all(true)
                                              .post();

        // then
        actualResponse.then()
                      .statusCode(HttpStatus.CREATED.value());
        then(userRepository.findById(actualResponse.body().as(DefaultPostResponse.class).getId()).orElse(null))
                .as("???????????? ?????? : %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForResponseWithId)
                .isEqualTo(expectedUser);
    }

    @ParameterizedTest
    @ArgumentsSource(SignUpViaEmailWhenUserAlreadyExistsProvider.class)
    @DisplayName("??????????????? ??????????????? ??? ?????? ??????????????? ?????? ????????? - ?????????")
    void signUpViaEmailWhenUserAlreadyExists(String description, SignUpRequest givenSignUpRequestAlreadyExists, ErrorResponse expectedResponse) {
        signUpWhenUserAlreadyExists(description, givenSignUpRequestAlreadyExists, expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(SignUpViaSocialWhenUserAlreadyExistsProvider.class)
    @DisplayName("??????????????? ??????????????? ??? ?????? ??????????????? ?????? ????????? - ??????")
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
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .contentType(ContentType.JSON)
                                                       .body(givenSignUpRequestAlreadyExists);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      SOCIAL_SIGN_UP_REQUEST_FIELDS,
                                                      ERROR_RESPONSE_FIELDS
                                              ))
                                              .log().all(true)
                                              .post();

        // then
        actualResponse.then()
                      .statusCode(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("???????????? ?????? - %s", description)
                .usingRecursiveComparison()
                .ignoringFields(new String[]{"timestamp"})
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(SignUpWithValidationProvider.class)
    @DisplayName("???????????? DTO validation")
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
                .as("???????????? ?????? : %s", description)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateEmailProvider.class)
    @DisplayName("?????? ????????? ???????????? ????????? ?????? ???????????? ?????????????????? ???????????? ????????????")
    void validateEmail(String description, String givenEmail, EmailValidationResponse expectedEmailValidationResponse) {
        // given
        String path = "/api/users/validate/email";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .queryParam("email", givenEmail);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      EMAIL_VALIDATION_REQUEST_PARAMS,
                                                      EMAIL_VALIDATION_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get()
                                              .andReturn();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(EmailValidationResponse.class))
                .as("????????? ?????? ??????: %s", description)
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
                .as("????????? ?????? ??????: %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateNicknameProvider.class)
    @DisplayName("?????? ????????? ???????????? ????????? ?????? ???????????? ?????????????????? ???????????? ????????????")
    void validateNickname(String description, String givenNickname, NicknameValidationResponse expectedNicknameValidationResponse) {
        // given
        String path = "/api/users/validate/nickname";
        RequestSpecification givenRequest = RestAssured.given(this.spec)
                                                       .baseUri(BASE_URL)
                                                       .port(port)
                                                       .basePath(path)
                                                       .queryParam("nickname", givenNickname);

        // when
        Response actualResponse = givenRequest.when()
                                              .filter(document(
                                                      DEFAULT_RESTDOCS_PATH,
                                                      NICKNAME_VALIDATION_REQUEST_PARAMS,
                                                      NICKNAME_VALIDATION_RESPONSE_FIELDS
                                              ))
                                              .log().all()
                                              .get()
                                              .andReturn();

        // then
        actualResponse.then()
                      .log().all()
                      .statusCode(HttpStatus.OK.value());
        then(actualResponse.as(NicknameValidationResponse.class))
                .as("????????? ?????? ??????: %s", description)
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
                .as("????????? ?????? ??????: ", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }
}
