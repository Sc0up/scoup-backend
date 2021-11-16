package com.postsquad.scoup.web.signin.service;

import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
import com.postsquad.scoup.web.signin.exception.UserNotFoundException;
import com.postsquad.scoup.web.signin.mapper.SignInResponseMapper;
import com.postsquad.scoup.web.user.domain.User;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInService {

    private final UserRepository userRepository;

    private final SignInTokenGenerator signInTokenGenerator;

    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.getEmail())
                                  .orElseThrow(() -> new UserNotFoundException(signInRequest.getEmail()));

        if (!user.getPassword().equals(signInRequest.getPassword())) {
            // TODO: 2-2-3에서 검증
        }

        String accessToken = signInTokenGenerator.accessToken(user.getId());
        String refreshToken = signInTokenGenerator.refreshToken(user.getId());

        return SignInResponseMapper.INSTANCE.map(user, accessToken, refreshToken);
    }
}
