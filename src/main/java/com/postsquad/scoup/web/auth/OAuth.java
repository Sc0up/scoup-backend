package com.postsquad.scoup.web.auth;

import com.postsquad.scoup.web.auth.controller.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.controller.response.OAuthUserResponse;

public interface OAuth {

    AccessTokenResponse getToken(String code);

    OAuthUserResponse getOAuthUserInfo(String token);

    OAuthUserResponse getOAuthUserInfoFromHeader(String header);
}
