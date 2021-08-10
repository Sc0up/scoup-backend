package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.EmailValidator;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public EmailValidationResponse validateEmail(String email) {
        if (EmailValidator.isValid(email)) {
            if (userRepository.findByEmail(email).isPresent()) {
                return EmailValidationResponse.of(true);
            }
            return EmailValidationResponse.of(false);
        }

        throw new IllegalArgumentException("Email is not valid");
    }
}
