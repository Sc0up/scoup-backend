package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.user.controller.request.SignUpRequest;

public class UserFactory {
    private UserFactory() {
    }

    public static User from(SignUpRequest signUpRequest) {
        return User.builder()
                .nickname(signUpRequest.getNickname())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .avatarUrl(signUpRequest.getAvatarUrl())
                .build();
    }
}
