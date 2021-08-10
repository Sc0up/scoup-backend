package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private static User TEST_USER;

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

        TEST_USER = User.of("email@email.com");

        Mockito.when(userRepository.findByEmail(TEST_USER.getEmail()))
                .thenReturn(Optional.of(TEST_USER));
    }

    @ParameterizedTest
    @MethodSource
    public void validateEmailTest(String email, EmailValidationResponse expected){
        // when
        EmailValidationResponse response = userService.validateEmail(email);

        // then
        then(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    static Stream<Arguments> validateEmailTest() {
        return Stream.of(
                Arguments.of("email@email.com", EmailValidationResponse.builder().existingEmail(true).build()),
                Arguments.of("notExistingEmail@email.com", EmailValidationResponse.builder().existingEmail(false).build())
        );
    }
}
