package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ValidateEmailProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공: 중복된 이메일",
                        "existing@email.com",
                        EmailValidationResponse.valueOf(true)
                ), Arguments.of(
                        "성공: 중복되지 않은 이메일",
                        "notExisting@email.com",
                        EmailValidationResponse.valueOf(false)
                )
        );
    }
}
