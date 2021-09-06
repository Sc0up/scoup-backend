package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oauth_type"}, name = "UK_OAUTH_TYPE"),
        @UniqueConstraint(columnNames = {"social_service_id"}, name = "UK_SOCIAL_SERVICE_ID"),
})
@Embeddable
public class OAuthUser {

    @Column(name = "oauth_type")
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

    public String getOAuthTypeName() {
        return oAuthType.name();
    }
}
