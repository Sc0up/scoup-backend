package com.postsquad.scoup.web.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String nickname;

    @Column(length = 20)
    private String username;

    @Column(length = 255)
    private String email;

    @Column(length = 1000)
    private String avatarUrl;

    @Column(length = 30)
    private String password;


    @Builder
    public User(Long id, String nickname, String username, String email, String avatarUrl, String password) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
    }
}
