package com.postsquad.scoup.web.user.auth;

import com.postsquad.scoup.web.user.auth.response.AccessTokenResponse;
import com.postsquad.scoup.web.user.auth.response.OAuthUserResponse;

public interface OAuth {

    AccessTokenResponse getToken(String code);

    OAuthUserResponse getUserInfo(String token);
}
