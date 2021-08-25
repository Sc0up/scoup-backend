package com.postsquad.scoup.web.auth.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserResponse {

    private String sub;

    private String name;

    private String email;

    private String picture;
}
