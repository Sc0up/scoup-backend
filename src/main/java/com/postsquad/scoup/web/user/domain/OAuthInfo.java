package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.auth.OAuthType;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
public class OAuthInfo {

    @Enumerated(EnumType.STRING)
    private OAuthType oAuthType;

    private String socialServiceId;
}
