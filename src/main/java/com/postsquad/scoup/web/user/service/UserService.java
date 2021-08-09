package com.postsquad.scoup.web.user.service;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;
import com.postsquad.scoup.web.user.domain.UserFactory;
import com.postsquad.scoup.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public long signIn(SignUpRequest signUpRequest) {
        return userRepository.save(UserFactory.from(signUpRequest)).getId();
    }
}
