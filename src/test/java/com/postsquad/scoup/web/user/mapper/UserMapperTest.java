package com.postsquad.scoup.web.user.mapper;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class UserMapperTest {

    @ParameterizedTest
    @MethodSource("signUpRequestToUserProvider")
    void signUpRequestToUser(SignUpRequest givenSignUpRequest, User expectedUser) {
        //when
        User actualUser = UserMapper.INSTANCE.map(givenSignUpRequest);

        //then
        then(actualUser).usingRecursiveComparison()
                        .isEqualTo(expectedUser);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> signUpRequestToUserProvider() {
        return Stream.of(
                Arguments.arguments(
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.NONE)
                                     .socialServiceId("")
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email")
                                     .password("password")
                                     .build(),
                        User.builder()
                            .nickname("nickname")
                            .username("username")
                            .email("email")
                            .password("password")
                            .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                            .build()
                ),
                Arguments.arguments(
                        SignUpRequest.builder()
                                     .oAuthType(OAuthType.GITHUB)
                                     .socialServiceId("1234567")
                                     .nickname("nickname")
                                     .username("username")
                                     .email("email@email")
                                     .password("password")
                                     .avatarUrl("https://avatars.githubusercontent.com/u/68000537?v=4")
                                     .build(),
                        User.builder()
                            .nickname("nickname")
                            .username("username")
                            .email("email@email")
                            .password("password")
                            .avatarUrl("https://avatars.githubusercontent.com/u/68000537?v=4")
                            .oAuthUsers(List.of(OAuthUser.of(OAuthType.GITHUB, "1234567")))
                            .build()
                )
        );
    }
}
