package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.mapper.UserMapper;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public EmailValidationResponse validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return EmailValidationResponse.valueOf(true);
        }
        return EmailValidationResponse.valueOf(false);
    }

    public long signIn(SignUpRequest signUpRequest) {
        return userRepository.save(UserMapper.INSTANCE.signUpRequestToUser(signUpRequest)).getId();
    }
}
