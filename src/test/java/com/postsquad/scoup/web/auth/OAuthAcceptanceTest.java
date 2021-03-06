package com.postsquad.scoup.web.auth;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.auth.controller.response.SocialAuthenticationResponse;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

class OAuthAcceptanceTest extends AcceptanceTestBase {

    @Value("${github.access.token}")
    private String accessToken;

    @DisplayName("소셜 서비스에 가입된 사용자의 정보를 불러올 수 있다")
    @ParameterizedTest
    @MethodSource("readUserDataProvider")
    void readUserData(String description, SocialAuthenticationResponse expectedSocialAuthenticationResponse) {
        // given
        String socialAuthenticationPath = "/api/oauth/user-data/token";
        RequestSpecification givenRequest = given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(socialAuthenticationPath)
                .queryParam("type", "GITHUB")
                .header(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all()
                                              .get()
                                              .andReturn();

        // then
        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(actualResponse.as(SocialAuthenticationResponse.class))
                .as("사용자의 소셜 계정 개인 정보 조회 : %s", description)
                .usingRecursiveComparison()
                .isEqualTo(expectedSocialAuthenticationResponse);
    }

    private static Stream<Arguments> readUserDataProvider() {
        return Stream.of(
                Arguments.of(
                        "성공",
                        SocialAuthenticationResponse.builder()
                                                    .oauthType(OAuthType.GITHUB)
                                                    .socialServiceId("68000537")
                                                    .name("JiSun Lim")
                                                    .email("jisunlim818@gmail.com")
                                                    .avatarUrl("https://avatars.githubusercontent.com/u/68000537?v=4")
                                                    .build()
                )
        );
    }

    @DisplayName("Github 요청 validation")
    @ParameterizedTest
    @MethodSource("gitHubRequestValidationProvider")
    void gitHubRequestValidation(String description, String invalidToken, ErrorResponse expectedResponse) {
        // given
        String socialAuthenticationPath = "/api/oauth/user-data/token";
        RequestSpecification givenRequest = given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(socialAuthenticationPath)
                .queryParam("type", "GITHUB")
                .header(HttpHeaders.AUTHORIZATION, "token " + invalidToken);

        // when
        Response actualResponse = givenRequest.when()
                                              .log().all(true)
                                              .get();

        // then
        assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(actualResponse.as(ErrorResponse.class))
                .as("사용자의 소셜 계정 개인 정보 조회 : %s", description)
                .usingRecursiveComparison()
                .ignoringFields(ignoringFieldsForErrorResponse)
                .isEqualTo(expectedResponse);
    }

    private static Stream<Arguments> gitHubRequestValidationProvider() {
        return Stream.of(
                Arguments.of(
                        "실패",
                        "gho_fehklwekjdojfwofjwfowg",
                        ErrorResponse.builder()
                                     .statusCode(HttpStatus.BAD_REQUEST.value())
                                     .message("Failed to get user data from the resource server")
                                     .errors(List.of("OAuth request fails validation"))
                                     .build()
                )
        );
    }
}
