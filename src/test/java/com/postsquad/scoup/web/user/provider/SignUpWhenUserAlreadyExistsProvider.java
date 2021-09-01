package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.stream.Stream;

public class SignUpWhenUserAlreadyExistsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패 - 이미 가입한 이메일(email@email)",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .username("username")
                                     .nickname("nickname")
                                     .email("existing@email.com")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Sign up failed")
                                     .statusCode(400)
                                     .errors(Arrays.asList("User email 'existing@email.com' already exists"))
                                     .build()
                ), Arguments.of(
                        "실패 - 이미 가입한 닉네임(nickname)",
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .username("username")
                                     .nickname("existing")
                                     .email("email2@email")
                                     .password("password")
                                     .build(),
                        ErrorResponse.builder()
                                     .message("Sign up failed")
                                     .statusCode(400)
                                     .errors(Arrays.asList("User nickname 'existing' already exists"))
                                     .build()
                )
        );
    }
}
