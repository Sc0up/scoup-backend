package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class SignUpViaSocialWhenUserAlreadyExistsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패 - 이미 가입한 소셜 서비스",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.GITHUB)
                                     .socialServiceId("1234567")
                                     .username("username")
                                     .nickname("existing")
                                     .email("email2@email")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Sign up failed")
                                     .statusCode(400)
                                     .errors(List.of("GITHUB account already exists"))
                                     .build()
                )
        );
    }
}
