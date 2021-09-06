package com.postsquad.scoup.web.signin.service;

import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.exception.UserNotFoundException;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;


@SpringBootTest(classes = {SignInService.class, SignInTokenGenerator.class})
class SignInServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    SignInTokenGenerator signInTokenGenerator;

    SignInService signInService;

    @BeforeEach
    void setUp() {
        signInService = new SignInService(userRepository, signInTokenGenerator);
    }

    @Test
    void signInThrowsUserNotFoundException() {
        String notExistingEmail = "not-existing@email.com";
        given(userRepository.findByEmail(notExistingEmail))
                .willReturn(Optional.empty());

        thenThrownBy(() -> signInService.signIn(SignInRequest.builder().email(notExistingEmail).build()))
                .isInstanceOf(UserNotFoundException.class);
    }
}
