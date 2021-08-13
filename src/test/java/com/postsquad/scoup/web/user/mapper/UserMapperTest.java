package com.postsquad.scoup.web.user.mapper;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.User;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

class UserMapperTest {

    @ParameterizedTest
    @MethodSource("signUpRequestToUserProvider")
    void signUpRequestToUser(SignUpRequest givenSignUpRequest, User expectedUser) {
        //when
        User actualUser = UserMapper.INSTANCE.signUpRequestToUser(givenSignUpRequest);

        //then
        then(actualUser).usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> signUpRequestToUserProvider() {
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
