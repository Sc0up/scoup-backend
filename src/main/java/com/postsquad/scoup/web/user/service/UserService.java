package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.domain.UserFactory;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public long signUp(SignUpRequest signUpRequest) {
        User user = UserFactory.from(signUpRequest);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(existingUser.get());
        }

        return userRepository.save(UserFactory.from(signUpRequest)).getId();
    }

    public EmailValidationResponse validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return EmailValidationResponse.valueOf(true);
        }
        return EmailValidationResponse.valueOf(false);
    }
}
