package com.postsquad.scoup.web.user.controller.response;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.Getter;

@Getter
public class SignInResponse {

    private String jwt;

    private String nickname;

    private String email;

    private ImageResponse avatarUrl;
}
