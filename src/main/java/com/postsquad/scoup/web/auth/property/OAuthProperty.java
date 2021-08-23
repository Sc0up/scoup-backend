package com.postsquad.scoup.web.auth.property;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;

@Getter
@PropertySource(value = "classpath:auth.properties")
public abstract class OAuthProperty {
    public static final String TOKEN = "token";

    protected String accessTokenUri;

    protected String redirectUri;

    protected String userUri;

    protected String clientId;

    protected String clientSecret;

    public OAuthProperty(String accessTokenUri, String redirectUri, String userUri, String clientId, String clientSecret) {
        this.accessTokenUri = accessTokenUri;
        this.redirectUri = redirectUri;
        this.userUri = userUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
