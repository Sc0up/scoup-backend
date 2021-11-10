package com.postsquad.scoup.web.user.provider;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class EmailSignUpProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(
                        "성공",
                        SignUpRequest.builder()
                                     .oauthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email@email")
                                     .password("password")
                                     .build(),
                        User.builder()
                            .nickname("nickname")
                            .username("username")
                            .email("email@email")
                            .password("password")
                            .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                            .build()
                )
        );
    }
}
