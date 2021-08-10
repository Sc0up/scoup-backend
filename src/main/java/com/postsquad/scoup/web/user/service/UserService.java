package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public EmailValidationResponse validateEmail(@Email String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return EmailValidationResponse.builder().existingEmail(true).build();
        }
        return EmailValidationResponse.builder().existingEmail(false).build();
    }
}
