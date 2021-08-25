package com.postsquad.scoup.web.signin.service;

import com.nimbusds.jose.JOSEException;
import com.postsquad.scoup.web.signin.controller.SignInTokenGenerator;
import com.postsquad.scoup.web.signin.controller.request.SignInRequest;
import com.postsquad.scoup.web.signin.controller.response.SignInResponse;
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

    // TODO: JOSEException SignInTokenGenerator안에서 끝내기
    public SignInResponse signIn(SignInRequest signInRequest) throws JOSEException {

        User user = userRepository.findByEmail(signInRequest.getEmail())
                                  // TODO: 2-2-2에서 검증
                                  .orElseThrow();

        if (!user.getPassword().equals(signInRequest.getPassword())) {
            // TODO: 2-2-2에서 검증
        }

        String accessToken = signInTokenGenerator.accessToken(user.getId());
        String refreshToken = signInTokenGenerator.refreshToken(user.getId());

        return SignInResponseMapper.INSTANCE.userToSignInResponse(user, accessToken, refreshToken);
    }
}
