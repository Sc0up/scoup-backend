package com.postsquad.scoup.web.user.domain;

import com.postsquad.scoup.web.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

    protected User(String nickname, String username, String email, String avatarUrl, String password) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
    }

    @Builder
    public static User of(String nickname, String username, String email, String avatarUrl, String password) {
        return new User(nickname, username, email, avatarUrl, password);
    }
}
