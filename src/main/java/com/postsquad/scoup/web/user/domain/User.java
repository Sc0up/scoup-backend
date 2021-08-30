package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}, name = "UK_USER_NICKNAME"),
        @UniqueConstraint(columnNames = {"email"}, name = "UK_USER_EMAIL"),
})
@Entity
public class User extends BaseEntity {

    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 1000)
    private String avatarUrl;

    @Column(length = 30, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private List<OAuthInfo> oAuthInfo = new ArrayList<>();

    protected User(String nickname, String username, String email, String avatarUrl, String password, List<OAuthInfo> oAuthInfo) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
        if (oAuthInfo != null) {
            this.oAuthInfo = oAuthInfo;
        }
    }

    @Builder
    public static User of(String nickname, String username, String email, String avatarUrl, String password, List<OAuthInfo> oAuthInfo) {
        return new User(nickname, username, email, avatarUrl, password, oAuthInfo);
    }
}
