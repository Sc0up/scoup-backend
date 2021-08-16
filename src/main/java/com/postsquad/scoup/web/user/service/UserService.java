package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.mapper.UserMapper;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public long signUp(SignUpRequest signUpRequest) {
        User user = UserMapper.INSTANCE.signUpRequestToUser(signUpRequest);

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(existingUser.get());
        }

        if (userRepository.existsByNickname(user.getNickname())) {
            throw new NicknameAlreadyExistsException(user);
        }

        return userRepository.save(user).getId();
    }

    public EmailValidationResponse validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return EmailValidationResponse.valueOf(true);
        }
        return EmailValidationResponse.valueOf(false);
    }
}
