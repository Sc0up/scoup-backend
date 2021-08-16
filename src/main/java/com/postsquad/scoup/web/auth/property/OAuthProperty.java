package com.postsquad.scoup.web.auth.property;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;


@Getter
@PropertySource(value = "classpath:auth.properties")
public class OAuthProperty {

    protected String accessTokenUri;

    protected String userUri;

    protected String clientId;

    protected String clientSecret;

    public OAuthProperty(String accessTokenUri, String userUri, String clientId, String clientSecret) {
        this.accessTokenUri = accessTokenUri;
        this.userUri = userUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
