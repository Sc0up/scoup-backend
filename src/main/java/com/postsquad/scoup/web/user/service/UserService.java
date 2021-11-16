package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.common.DefaultPostResponse;
import com.postsquad.scoup.web.user.controller.EmailValidationRequest;
import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.controller.response.EmailValidationResponse;
import com.postsquad.scoup.web.user.controller.response.NicknameValidationResponse;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.mapper.UserMapper;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public DefaultPostResponse signUp(SignUpRequest signUpRequest) {
        User user = UserMapper.INSTANCE.map(signUpRequest);

        if (user.isOAuthUser() && userRepository.existsByOAuthUser(user.getFirstRegisteredOAuthUser())) {
            throw new OAuthUserAlreadyExistsException(user);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user);
        }

        if (userRepository.existsByNickname(user.getNickname())) {
            throw new NicknameAlreadyExistsException(user);
        }

        return DefaultPostResponse.builder()
                                  .id(userRepository.save(user).getId())
                                  .build();
    }

    public EmailValidationResponse validateEmail(EmailValidationRequest emailValidationRequest) {
        if (userRepository.findByEmail(emailValidationRequest.getEmail()).isPresent()) {
            return EmailValidationResponse.valueOf(true);
        }
        return EmailValidationResponse.valueOf(false);
    }

    public NicknameValidationResponse validateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            return NicknameValidationResponse.valueOf(true);
        }
        return NicknameValidationResponse.valueOf(false);
    }
}
