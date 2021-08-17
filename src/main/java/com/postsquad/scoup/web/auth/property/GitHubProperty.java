package com.postsquad.scoup.web.auth.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GitHubProperty extends OAuthProperty {

    public GitHubProperty(@Value("${github.access.token.uri}") String accessTokenUri,
                          @Value("${github.web.redirect.uri}") String redirectUri,
                          @Value("${github.user.uri}") String userUri,
                          @Value("${github.web.client.id}") String clientId,
                          @Value("${github.web.client.secret}") String clientSecret) {
        super(accessTokenUri, redirectUri, userUri, clientId, clientSecret);
    }
}
