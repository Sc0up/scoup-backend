package com.postsquad.scoup.web.user;

import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.repository.UserRepository;
import com.postsquad.scoup.web.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private static User TEST_USER;

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

        TEST_USER = User.of("sally@gmail.com");

        Mockito.when(userRepository.findByEmail(TEST_USER.getEmail()))
                .thenReturn(Optional.of(TEST_USER));
    }

    @ParameterizedTest
    @MethodSource
    public void validateEmailTest(String email, EmailValidationResponse expected){
        EmailValidationResponse response = userService.validateEmail(email);

        assertThat(response).isEqualTo(expected);
    }

    static Stream<Arguments> validateEmailTest() {
        return Stream.of(
                Arguments.of("sally@gmail.com", EmailValidationResponse.of(true)),
                Arguments.of("jane@gmail.com", EmailValidationResponse.of(false))
        );
    }
}
