package com.postsquad.scoup.web.auth.controller.response;

import lombok.Getter;

@Getter
public class GoogleUserResponse {

    private String sub;

    private String name;

    private String email;

    private String picture;
}
