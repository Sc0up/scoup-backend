package com.postsquad.scoup.web.user.repository;

import com.postsquad.scoup.web.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        User user = User.builder()
                .nickname("nickname")
                .username("username")
                .email("email")
                .password("password")
                .build();

        userRepository.save(user);

        Assertions.assertThat(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @ParameterizedTest
    @MethodSource("saveWithNotnullViolationProvider")
    void saveWithNotnullViolation(User user) {
        Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(user));
    }

    static Stream<Arguments> saveWithNotnullViolationProvider() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .username("username")
                                .email("email")
                                .password("password")
                                .build()
                ), Arguments.of(
                        User.builder()
                                .nickname("nickname")
                                .email("email")
                                .password("password")
                                .build()
                ), Arguments.of(
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .password("password")
                                .build()
                ), Arguments.of(
                        User.builder()
                                .nickname("nickname")
                                .username("username")
                                .email("email")
                                .build()
                ), Arguments.of(
                        User.builder()
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("saveWithUniqueConstraintViolationProvider")
    void saveWithUniqueConstraintViolation(User firstUser, User secondUser) {
        userRepository.save(firstUser);

        Assertions.assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(secondUser));
    }

    static Stream<Arguments> saveWithUniqueConstraintViolationProvider() {
        return Stream.of(
                Arguments.of(
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
                )
        );
    }
}
