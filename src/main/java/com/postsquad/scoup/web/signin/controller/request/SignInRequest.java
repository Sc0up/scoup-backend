package com.postsquad.scoup.web.signin.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SignInRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

}
