package com.postsquad.scoup.web.signin.controller.response;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.*;

@Getter
public class SignInResponse {

    private String accessToken;

    private String nickname;

    private String email;

    private ImageResponse avatarUrl;
}
