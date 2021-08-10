package com.postsquad.scoup.web.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}, name = "UK_USER_NICKNAME"),
        @UniqueConstraint(columnNames = {"email"}, name = "UK_USER_EMAIL"),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    protected User(Long id, String nickname, String username, String email, String avatarUrl, String password) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
    }

    @Builder
    public static User of(Long id, String nickname, String username, String email, String avatarUrl, String password) {
        return new User(id, nickname, username, email, avatarUrl, password);
    }
}
