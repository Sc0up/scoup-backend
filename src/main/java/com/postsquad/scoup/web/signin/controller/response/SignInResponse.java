package com.postsquad.scoup.web.signin.controller.response;

import com.postsquad.scoup.web.image.controller.ImageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SignInResponse {

    private String accessToken;

    private String nickname;

    private String email;

    private ImageResponse avatarUrl;
}
