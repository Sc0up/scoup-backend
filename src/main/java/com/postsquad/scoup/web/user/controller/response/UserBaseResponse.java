package com.postsquad.scoup.web.user.controller.response;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.Getter;

@Getter
public class UserBaseResponse {

    private String nickname;

    private String username;

    private String email;

    private ImageResponse avatarUrl;

    private String password;
}
