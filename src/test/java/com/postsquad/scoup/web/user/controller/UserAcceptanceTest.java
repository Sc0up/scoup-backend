package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

public class UserAcceptanceTest extends AcceptanceTestBase {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(User.of("existing@email.com"));
    }

    @ParameterizedTest
    @MethodSource("validateEmailProvider")
    @DisplayName("이미 가입된 이메일을 입력할 경우 이메일이 중복되었다는 메시지가 반환된다.")
    void validateEmail(String desc, String givenEmailRequest, EmailValidationResponse expectedEmailResponse) {
        String path = "/api/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                    .baseUri(BASE_URL)
                                                    .port(port)
                                                    .basePath(path)
                                                    .queryParam("email", givenEmailRequest);

        Response actualResponse = givenRequest.when()
                                          .log().all()
                                          .get()
                                          .andReturn();

        then(actualResponse)
                .as("이메일 중복 확인: %s", desc)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .extracting(response -> response.as(EmailValidationResponse.class, restAssuredObjectMapper))
                .usingRecursiveComparison()
                .isEqualTo(expectedEmailResponse);
    }

    static Stream<Arguments> validateEmailProvider() {
        return Stream.of(
                Arguments.of("성공: 중복된 이메일",
                        "existing@email.com",
                        EmailValidationResponse.builder().existingEmail(true).build()
                ),
                Arguments.of("성공: 중복되지 않은 이메일",
                        "notExisting@email.com",
                        EmailValidationResponse.builder().existingEmail(false).build()
        ));
    }

    @ParameterizedTest
    @MethodSource("validateRequestParamProvider")
    @DisplayName("Request parameter validation")
    void validateRequestParam(String description, String givenEmailRequest, ErrorResponse expectedResponse) {
        String path = "/api/validate/email";
        RequestSpecification givenRequest = RestAssured.given()
                                                    .baseUri(BASE_URL)
                                                    .port(port)
                                                    .basePath(path)
                                                    .queryParam("email", givenEmailRequest);

        Response actualResponse = givenRequest.when()
                                          .log().all()
                                          .get()
                                          .andReturn();

        then(actualResponse)
                .as("이메일 중복 확인: %s", description)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST.value())
                .extracting(response -> response.as(ErrorResponse.class, restAssuredObjectMapper))
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
