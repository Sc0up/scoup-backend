package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.UserFactory;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public EmailValidationResponse validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return EmailValidationResponse.builder().existingEmail(true).build();
        }
        return EmailValidationResponse.builder().existingEmail(false).build();
    }

    public long signIn(SignUpRequest signUpRequest) {
        return userRepository.save(UserFactory.from(signUpRequest)).getId();
    }
}
