package com.postsquad.scoup.web.auth.property;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.postsquad.scoup.web.auth.OAuthType.*;

@Getter
@Component
public class OAuthProperties {

    private static final Map<OAuthType, OAuthProperty> PROPERTIES = new HashMap<>();

    private OAuthProperties(GitHubProperty gitHubProperty, KakaoProperty kakaoProperty, GoogleProperty googleProperty) {
        PROPERTIES.put(GITHUB, gitHubProperty);
        PROPERTIES.put(KAKAO, kakaoProperty);
        PROPERTIES.put(GOOGLE, googleProperty);
    }

    public OAuthProperty getProperty(OAuthType type) {
        return PROPERTIES.get(type);
    }
}
