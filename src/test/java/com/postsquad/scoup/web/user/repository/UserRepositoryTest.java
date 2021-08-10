package com.postsquad.scoup.web.user.repository;

import com.postsquad.scoup.web.user.domain.User;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        // given
        User user = User.builder()
                .nickname("nickname")
                .username("username")
                .email("email")
                .password("password")
                .build();

        // when
        userRepository.save(user);

        // then
        then(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @ParameterizedTest
    @MethodSource("saveWithNotnullViolationProvider")
    void saveWithNotnullViolation(String description, User givenUser) {
        // given

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> userRepository.save(givenUser);

        // then
        thenThrownBy(throwingCallable)
                .as("Not null 제약 : %s", description)
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> saveWithNotnullViolationProvider() {
        return Stream.of(
                Arguments.of(
                        "닉네임 없는 경우",
                        User.builder()
                                .username("username")
                                .email("email")
                                .password("password")
                                .build()
                ), Arguments.of(
                        "사용자명 없는 경우",
                        User.builder()
                                .nickname("nickname")
                                .email("email")
                                .password("password")
                                .build()
                ), Arguments.of(
                        "이메일 없는 경우",
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .password("password")
                                .build()
                ), Arguments.of(
                        "비밀번호 없는 경우",
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .build()
                ), Arguments.of(
                        "아무것도 없는 경우",
                        User.builder()
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("saveWithUniqueConstraintViolationProvider")
    void saveWithUniqueConstraintViolation(String description, User givenUser, User givenUserForThrowingException) {
        // given
        userRepository.save(givenUser);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> userRepository.save(givenUserForThrowingException);

        // then
        thenThrownBy(throwingCallable)
                .as("유니크 제약 : %s", description)
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> saveWithUniqueConstraintViolationProvider() {
        return Stream.of(
                Arguments.of(
                        "닉네임 중복",
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build(),
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email2")
                                .password("password")
                                .build()
                ), Arguments.of(
                        "이메일 중복",
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build(),
                        User.builder()
                                .nickname("nickname2")
                                .username("username")
                                .email("email")
                                .password("password")
                                .build()
                )
        );
    }
}
