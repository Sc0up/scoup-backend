package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class UserFactoryTest {

    @ParameterizedTest
    @MethodSource("fromSignUpRequestProvider")
    void fromSignUpRequest(SignUpRequest givenSignUpRequest, User expectedUser) {
        User actualUser = UserFactory.from(givenSignUpRequest);

        then(actualUser).usingRecursiveComparison()
                .isEqualTo(expectedUser);

    }

    static Stream<Arguments> fromSignUpRequestProvider() {
        return Stream.of(
                Arguments.arguments(
                        SignUpRequest.builder()
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
                                .build()
                )
        );
    }
}
