package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
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

        TEST_USER = User.builder().avatarUrl(null).email("email@email.com").nickname(null).password(null).username(null).build();

        Mockito.when(userRepository.findByEmail(TEST_USER.getEmail()))
               .thenReturn(Optional.of(TEST_USER));
    }

    @Test
    void signUpThrowsEmailAlreadyExistsException() {
        String existingEmail = "existing@email.com";
        given(userRepository.existsByEmail(existingEmail))
                .willReturn(true);

        thenThrownBy(() -> userService.signUp(SignUpRequest.builder().email(existingEmail).build()))
                // TODO: EmailAlreadyExistsException로 변경
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void signUpThrowsNicknameAlreadyExistsException() {
        String existingNickname = "existing";
        given(userRepository.existsByNickname(existingNickname))
                .willReturn(true);

        thenThrownBy(() -> userService.signUp(SignUpRequest.builder().nickname(existingNickname).build()))
                .isInstanceOf(NicknameAlreadyExistsException.class);
    }

    @ParameterizedTest
    @MethodSource("validateEmailProvider")
    public void validateEmail(String email, EmailValidationResponse expected) {
        // when
        EmailValidationResponse response = userService.validateEmail(email);

        // then
        then(response).usingRecursiveComparison()
                      .isEqualTo(expected);
    }

    static Stream<Arguments> validateEmailProvider() {
        return Stream.of(
                Arguments.of("email@email.com", EmailValidationResponse.valueOf(true)),
                Arguments.of("notExistingEmail@email.com", EmailValidationResponse.valueOf(false))
        );
    }
}
