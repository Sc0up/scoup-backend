package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.error.controller.response.ErrorResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.stream.Stream;

public class ValidateNicknameRequestParamProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "실패: 빈 문자열",
                        "",
                        ErrorResponse.builder()
                                     .statusCode(HttpStatus.BAD_REQUEST.value())
                                     .message("Method argument not valid.")
                                     .errors(Collections.singletonList("validateNickname.nickname: must not be empty"))
                                     .build()
                ),
                Arguments.of(
                        "실패: null",
                        null,
                        ErrorResponse.builder()
                                     .statusCode(HttpStatus.BAD_REQUEST.value())
                                     .message("Method argument not valid.")
                                     .errors(Collections.singletonList("validateNickname.nickname: must not be empty"))
                                     .build()
                )
        );
    }
}
