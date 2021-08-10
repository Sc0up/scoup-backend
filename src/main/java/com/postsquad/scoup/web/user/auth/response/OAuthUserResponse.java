package com.postsquad.scoup.web.user.auth.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthUserResponse {

    private Long id;
    private String login;
    private String email;
    private String avatarUrl;
}
