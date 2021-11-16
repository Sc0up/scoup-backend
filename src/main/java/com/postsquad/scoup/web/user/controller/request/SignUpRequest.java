package com.postsquad.scoup.web.user.controller.request;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SignUpRequest {

    @NotNull
    private OAuthType oauthType;

    @NotNull
    private String socialServiceId;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String avatarUrl;
}
