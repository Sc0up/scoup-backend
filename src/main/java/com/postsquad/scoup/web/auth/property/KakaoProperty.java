package com.postsquad.scoup.web.auth.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProperty extends OAuthProperty {

    public KakaoProperty(@Value("${kakao.access.token.uri}") String accessTokenUri,
                         @Value("${kakao.user.uri}") String userUri,
                         @Value("${kakao.web.client.id}") String clientId,
                         @Value("${kakao.web.client.secret}") String clientSecret) {
        super(accessTokenUri, userUri, clientId, clientSecret);
    }
}
