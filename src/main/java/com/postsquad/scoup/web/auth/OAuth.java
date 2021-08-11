package com.postsquad.scoup.web.auth;

import com.postsquad.scoup.web.auth.response.AccessTokenResponse;
import com.postsquad.scoup.web.auth.response.OAuthUserResponse;

public interface OAuth {

    AccessTokenResponse getToken(String code);

    OAuthUserResponse getOAuthUserInfo(String token);
}
