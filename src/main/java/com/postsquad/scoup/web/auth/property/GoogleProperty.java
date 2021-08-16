package com.postsquad.scoup.web.auth.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleProperty extends OAuthProperty {

    public GoogleProperty(@Value("${google.access.token.uri}") String accessTokenUri,
                          @Value("${google.user.uri}") String userUri,
                          @Value("${google.web.client.id}") String clientId,
                          @Value("${google.web.client.secret}") String clientSecret) {
        super(accessTokenUri, userUri, clientId, clientSecret);
    }
}
