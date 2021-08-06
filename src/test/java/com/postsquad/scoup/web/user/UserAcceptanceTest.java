package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.AcceptanceTestBase;
import com.postsquad.scoup.web.user.controller.response.SocialAuthenticationResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.BDDAssertions.then;

class UserAcceptanceTest extends AcceptanceTestBase {

    @DisplayName("소셜 서비스에 가입된 사용자의 정보를 불러올 수 있다.")
    @ParameterizedTest
    @MethodSource("readUserDataProvider")
    void readUserData(SocialAuthenticationResponse expectedSocialAuthenticationResponse) {
        // given
        String gitHubBaseUri = "https://github.com";
        String gitHubPath = "/login/oauth/authorize?client_id=";
        String clientId = "2abdddfdc8e14727fc07";
        String authorizationCode = given()
                .baseUri(gitHubBaseUri)
                .basePath(gitHubPath)
                .queryParam("client_id", clientId)
                .get()
                .asString();

        String socialAuthenticationPath = "/api/social/authenticate";
        RequestSpecification givenRequest = given()
                .baseUri(BASE_URL)
                .port(port)
                .basePath(socialAuthenticationPath)
                .queryParam("authorization_code", authorizationCode);

        // when
        Response actualResponse = givenRequest.when()
                .log().all()
                .get()
                .andReturn();

        // then
        then(actualResponse)
                .as("사용자의 소셜 계정 개인 정보 조회")
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK.value())
                .extracting(response -> response.as(SocialAuthenticationResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(expectedSocialAuthenticationResponse);
    }

    private static Stream<SocialAuthenticationResponse> readUserDataProvider() {
        return Stream.of(SocialAuthenticationResponse
                .builder()
                .username("janeljs")
                .email("jisunlim818@gmail.com")
                .avatarUrl("https://avatars.githubusercontent.com/u/68000537?v=4")
                .build());
    }
}
