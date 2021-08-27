package com.postsquad.scoup.web.signin.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String refreshToken;

    private String nickname;

    private String email;

    private String avatarUrl;
}
