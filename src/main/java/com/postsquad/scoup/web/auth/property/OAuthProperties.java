package com.postsquad.scoup.web.auth.property;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.postsquad.scoup.web.auth.property.OAuthType.*;

@Component
@Getter
public class OAuthProperties {

    private static final Map<OAuthType, OAuthProperty> PROPERTIES = new HashMap<>();

    public OAuthProperties(GitHubProperty gitHubProperty, KakaoProperty kakaoProperty, GoogleProperty googleProperty) {
        PROPERTIES.put(GITHUB, gitHubProperty);
        PROPERTIES.put(KAKAO, kakaoProperty);
        PROPERTIES.put(GOOGLE, googleProperty);
    }

    public OAuthProperty getProperty(String type) {
        OAuthType oAuthType = OAuthType.valueOf(type.toUpperCase());
        return PROPERTIES.get(oAuthType);
    }
}
