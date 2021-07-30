package com.postsquad.scoup.web.user.controller.request;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class SignUpRequest {

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    private ImageResponse avatarUrl;

    @NotEmpty
    private String password;
}
