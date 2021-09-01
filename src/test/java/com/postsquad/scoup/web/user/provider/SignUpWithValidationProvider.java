package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.stream.Stream;

public class SignUpWithValidationProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        return Stream.of(
                Arguments.of(
                        "실패 - nickname 없음",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .username("username")
                                     .email("email@email")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(400)
                                     .errors(Arrays.asList("nickname: 비어 있을 수 없습니다"))
                                     .build()
                ), Arguments.of(
                        "실패 - username 없음",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .email("email@email")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(400)
                                     .errors(Arrays.asList("username: 비어 있을 수 없습니다"))
                                     .build()
                ), Arguments.of(
                        "실패 - email 없음",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .username("username")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(400)
                                     .errors(Arrays.asList("email: 비어 있을 수 없습니다"))
                                     .build()
                ), Arguments.of(
                        "실패 - password 없음",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email@email")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(400)
                                     .errors(Arrays.asList("password: 비어 있을 수 없습니다"))
                                     .build()
                ), Arguments.of(
                        "실패 - email 형식 다름",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
                                     .statusCode(400)
                                     .errors(Arrays.asList("email: 올바른 형식의 이메일 주소여야 합니다"))
                                     .build()
                ), Arguments.of(
                        "실패 - 모두 없음",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Method argument not valid.")
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
