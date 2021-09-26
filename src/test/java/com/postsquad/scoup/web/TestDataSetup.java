package com.postsquad.scoup.web;

import com.postsquad.scoup.web.auth.OAuthType;
import com.postsquad.scoup.web.user.domain.OAuthUser;
import com.postsquad.scoup.web.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class TestDataSetup {

    @PersistenceContext
    private EntityManager entityManager;

    public void execute() {
        User user = User.builder()
                        .nickname("nickname")
                        .email("email@email.com")
                        .password("password")
                        .avatarUrl("url")
                        .username("username")
                        .oAuthUsers(List.of(OAuthUser.of(OAuthType.NONE, "")))
                        .build();
        entityManager.persist(user);
    }
}
