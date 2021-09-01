package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.user.controller.response.NicknameValidationResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ValidateNicknameProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공: 중복된 닉네임",
                        "existing",
                        NicknameValidationResponse.valueOf(true)
                ),
                Arguments.of(
                        "성공: 중복되지 않은 닉네임",
                        "notExistingNickname",
                        NicknameValidationResponse.valueOf(false)
                )
        );
    }
}
