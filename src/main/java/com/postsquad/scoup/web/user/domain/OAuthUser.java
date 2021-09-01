package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class OAuthUser {

    @Enumerated(EnumType.STRING)
    private OAuthType oAuthType;

    private String socialServiceId;

    protected OAuthUser(OAuthType oAuthType, String socialServiceId) {
        this.oAuthType = oAuthType;
        this.socialServiceId = socialServiceId;
    }

    @Builder
    public static OAuthUser of(OAuthType oAuthType, String socialServiceId) {
        return new OAuthUser(oAuthType, socialServiceId);
    }
}
