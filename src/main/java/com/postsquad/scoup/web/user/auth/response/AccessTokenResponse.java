package com.postsquad.scoup.web.user.auth.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponse {

    private String accessToken;
    private String tokenType;
    private String scope;
}
